package com.casewire.soc.event;

import com.casewire.soc.alert.BruteForceDetectionRule;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {
    private static final Pattern EVENT_TYPE_PATTERN = Pattern.compile("[A-Z][A-Z0-9_]{0,63}");
    private final NormalizedEventRepository repository;
    private final BruteForceDetectionRule bruteForceRule;

    public EventService(NormalizedEventRepository repository, BruteForceDetectionRule bruteForceRule) {
        this.repository = repository;
        this.bruteForceRule = bruteForceRule;
    }

    @Transactional
    public NormalizedEvent importEvent(EventImportRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("event request is required");
        }
        if (request.getEventTimestamp() == null) {
            throw new IllegalArgumentException("eventTimestamp is required");
        }

        String eventType = request.getEventType();
        if (eventType == null || !EVENT_TYPE_PATTERN.matcher(eventType).matches()) {
            throw new IllegalArgumentException(
                    "eventType must be 1-64 characters using uppercase letters, digits, and underscores");
        }

        validateRequiredText("sourceReferenceId", request.getSourceReferenceId(), 128);
        validateOptionalText("username", request.getUsername(), 128);
        validateOptionalText("hostname", request.getHostname(), 253);
        validateIpAddress("sourceIp", request.getSourceIp());
        validateIpAddress("destinationIp", request.getDestinationIp());

        NormalizedEvent event = new NormalizedEvent();
        event.setEventTimestamp(request.getEventTimestamp());
        event.setEventType(eventType);
        event.setSourceIp(request.getSourceIp());
        event.setDestinationIp(request.getDestinationIp());
        event.setUsername(request.getUsername());
        event.setHostname(request.getHostname());
        event.setSourceReferenceId(request.getSourceReferenceId());
        NormalizedEvent savedEvent = repository.save(event);
        // Synchronous rule evaluation; alert timestamps are based on event time
        // so replaying the same historical events produces a deterministic timeline.
        bruteForceRule.evaluate(savedEvent);
        return savedEvent;
    }

    private static void validateRequiredText(String field, String value, int maxLength) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        if (value.length() > maxLength) {
            throw new IllegalArgumentException(field + " must be at most " + maxLength + " characters");
        }
        if (!value.equals(value.trim())) {
            throw new IllegalArgumentException(field + " must not have leading or trailing whitespace");
        }
    }

    private static void validateOptionalText(String field, String value, int maxLength) {
        if (value == null) {
            return;
        }
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank when provided");
        }
        if (value.length() > maxLength) {
            throw new IllegalArgumentException(field + " must be at most " + maxLength + " characters");
        }
        if (!value.equals(value.trim())) {
            throw new IllegalArgumentException(field + " must not have leading or trailing whitespace");
        }
    }

    private static void validateIpAddress(String field, String value) {
        if (value == null) {
            return;
        }
        if (value.isBlank() || value.length() > 45 || !value.equals(value.trim()) || value.contains("%")) {
            throw new IllegalArgumentException(field + " must be a valid IPv4 or IPv6 literal");
        }

        if (!value.contains(":")) {
            String[] octets = value.split("\\.", -1);
            if (octets.length != 4) {
                throw new IllegalArgumentException(field + " must be a valid IPv4 or IPv6 literal");
            }
            for (String octet : octets) {
                if (octet.isEmpty() || !octet.chars().allMatch(Character::isDigit)) {
                    throw new IllegalArgumentException(field + " must be a valid IPv4 or IPv6 literal");
                }
                try {
                    int number = Integer.parseInt(octet);
                    if (number < 0 || number > 255) {
                        throw new IllegalArgumentException(field + " must be a valid IPv4 or IPv6 literal");
                    }
                } catch (NumberFormatException exception) {
                    throw new IllegalArgumentException(field + " must be a valid IPv4 or IPv6 literal");
                }
            }
            return;
        }
        try {
            if (!(InetAddress.getByName(value) instanceof Inet6Address)) {
                throw new IllegalArgumentException(field + " must be a valid IPv4 or IPv6 literal");
            }
        } catch (UnknownHostException exception) {
            throw new IllegalArgumentException(field + " must be a valid IPv4 or IPv6 literal");
        }
    }
}
