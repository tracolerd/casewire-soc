package com.casewire.soc.alert;
import com.casewire.soc.event.EventStore;
import com.casewire.soc.event.NormalizedEvent;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
class BruteForceDetectionRuleTest {
    @Test
    void shouldTriggerAlertWhenThresholdExceeded() {
        EventStore mockStore = mock(EventStore.class);
        AlertGroupRepository mockRepo = mock(AlertGroupRepository.class);
        BruteForceDetectionRule rule = new BruteForceDetectionRule(mockStore, mockRepo);
        NormalizedEvent latestEvent = new NormalizedEvent();
        latestEvent.setEventType("FAILED_LOGIN");
        latestEvent.setSourceIp("192.168.1.50");
        Instant eventTime = Instant.parse("2026-10-23T09:30:00Z");
        latestEvent.setEventTimestamp(eventTime);
        // Mock 3 events found in DB
        when(mockStore.findEventsByTypeAndIpWithinTimeWindow(any(), any(), any(), any()))
                .thenReturn(List.of(new NormalizedEvent(), new NormalizedEvent(), new NormalizedEvent()));
        when(mockRepo.save(any(AlertGroup.class))).thenAnswer(i -> i.getArguments()[0]);
        Optional<AlertGroup> alert = rule.evaluate(latestEvent);
        assertTrue(alert.isPresent());
        assertEquals("192.168.1.50", alert.get().getGroupingKey());
        assertEquals(eventTime, alert.get().getTriggeredAt());
        assertTrue(alert.get().getDescription().contains("Detected 3"));
    }
    @Test
    void shouldNotTriggerAlertIfBelowThreshold() {
        EventStore mockStore = mock(EventStore.class);
        AlertGroupRepository mockRepo = mock(AlertGroupRepository.class);
        BruteForceDetectionRule rule = new BruteForceDetectionRule(mockStore, mockRepo);
        NormalizedEvent latestEvent = new NormalizedEvent();
        latestEvent.setEventType("FAILED_LOGIN");
        latestEvent.setSourceIp("192.168.1.50");
        latestEvent.setEventTimestamp(Instant.now());
        // Mock only 2 events found
        when(mockStore.findEventsByTypeAndIpWithinTimeWindow(any(), any(), any(), any()))
                .thenReturn(List.of(new NormalizedEvent(), new NormalizedEvent()));
        Optional<AlertGroup> alert = rule.evaluate(latestEvent);
        assertTrue(alert.isEmpty());
    }
}