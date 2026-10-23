package com.casewire.soc.event;
import com.casewire.soc.alert.BruteForceDetectionRule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class EventService {
    private final NormalizedEventRepository repository;
    private final BruteForceDetectionRule bruteForceRule;
    public EventService(NormalizedEventRepository repository, BruteForceDetectionRule bruteForceRule) {
        this.repository = repository;
        this.bruteForceRule = bruteForceRule;
    }
    @Transactional
    public NormalizedEvent importEvent(EventImportRequest request) {
        // Fast-fail validations
        if (request.getEventTimestamp() == null) throw new IllegalArgumentException("eventTimestamp is required");
        if (request.getEventType() == null || request.getEventType().isBlank()) throw new IllegalArgumentException("eventType is required");
        if (request.getSourceReferenceId() == null || request.getSourceReferenceId().isBlank()) throw new IllegalArgumentException("sourceReferenceId is required");
        NormalizedEvent event = new NormalizedEvent();
        event.setEventTimestamp(request.getEventTimestamp());
        event.setEventType(request.getEventType());
        event.setSourceIp(request.getSourceIp());
        event.setDestinationIp(request.getDestinationIp());
        event.setUsername(request.getUsername());
        event.setHostname(request.getHostname());
        event.setSourceReferenceId(request.getSourceReferenceId());
        NormalizedEvent savedEvent = repository.save(event);
        // Synchronous rule evaluation
        bruteForceRule.evaluate(savedEvent);
        return savedEvent;
    }
}