package com.casewire.soc.alert;
import com.casewire.soc.event.EventStore;
import com.casewire.soc.event.NormalizedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
@Service
public class BruteForceDetectionRule {
    private final EventStore eventStore;
    private final AlertGroupRepository alertRepository;
    private static final String TARGET_EVENT_TYPE = "FAILED_LOGIN";
    private static final int THRESHOLD = 3;
    private static final Duration WINDOW_DURATION = Duration.ofMinutes(5);
    public BruteForceDetectionRule(EventStore eventStore, AlertGroupRepository alertRepository) {
        this.eventStore = eventStore;
        this.alertRepository = alertRepository;
    }
    @Transactional
    public Optional<AlertGroup> evaluate(NormalizedEvent latestEvent) {
        if (!TARGET_EVENT_TYPE.equals(latestEvent.getEventType()) || latestEvent.getSourceIp() == null) {
            return Optional.empty();
        }
        Instant endTime = latestEvent.getEventTimestamp();
        Instant startTime = endTime.minus(WINDOW_DURATION);
        String sourceIp = latestEvent.getSourceIp();
        List<NormalizedEvent> recentFailures = eventStore.findEventsByTypeAndIpWithinTimeWindow(
                TARGET_EVENT_TYPE, sourceIp, startTime, endTime);
        if (recentFailures.size() >= THRESHOLD) {
            // Do not create a new alert for every additional failure in the
            // same suppression window. Use source event time for deterministic
            // replay; the database query bounds the lookup to this IP/rule.
            Optional<AlertGroup> existingAlert = alertRepository
                    .findFirstByRuleNameAndGroupingKeyAndTriggeredAtGreaterThanEqualAndTriggeredAtLessThanEqualOrderByTriggeredAtDesc(
                            "Brute Force Authentication Detected",
                            sourceIp,
                            startTime,
                            endTime.plus(WINDOW_DURATION));
            if (existingAlert.isPresent()) {
                return Optional.empty();
            }
            AlertGroup alert = new AlertGroup();
            alert.setRuleName("Brute Force Authentication Detected");
            alert.setGroupingKey(sourceIp);
            alert.setTriggeredAt(endTime);
            alert.setDescription(String.format("Detected %d FAILED_LOGIN events from IP: %s within 5 minutes.", recentFailures.size(), sourceIp));
            return Optional.of(alertRepository.save(alert));
        }
        return Optional.empty();
    }
}