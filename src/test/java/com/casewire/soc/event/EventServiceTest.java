package com.casewire.soc.event;
import com.casewire.soc.alert.BruteForceDetectionRule;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
class EventServiceTest {
    @Test
    void shouldSaveEventAndTriggerRule() {
        NormalizedEventRepository mockRepo = mock(NormalizedEventRepository.class);
        BruteForceDetectionRule mockRule = mock(BruteForceDetectionRule.class);
        EventService service = new EventService(mockRepo, mockRule);
        EventImportRequest req = new EventImportRequest();
        req.setEventTimestamp(Instant.now());
        req.setEventType("FAILED_LOGIN");
        req.setSourceReferenceId("log-123");
        NormalizedEvent savedMock = new NormalizedEvent();
        when(mockRepo.save(any(NormalizedEvent.class))).thenReturn(savedMock);
        service.importEvent(req);
        // Verify repository saved the event
        verify(mockRepo, times(1)).save(any(NormalizedEvent.class));
        // Verify rule engine was called with the saved event
        verify(mockRule, times(1)).evaluate(savedMock);
    }
    @Test
    void shouldRejectMalformedEventFieldsBeforePersistence() {
        NormalizedEventRepository mockRepo = mock(NormalizedEventRepository.class);
        BruteForceDetectionRule mockRule = mock(BruteForceDetectionRule.class);
        EventService service = new EventService(mockRepo, mockRule);

        EventImportRequest request = new EventImportRequest();
        request.setEventTimestamp(Instant.parse("2026-10-23T09:30:00Z"));
        request.setEventType("FAILED_LOGIN");
        request.setSourceReferenceId("log-123");
        request.setSourceIp("999.1.1.1");

        assertThrows(IllegalArgumentException.class, () -> service.importEvent(request));
        verify(mockRepo, never()).save(any());
        verify(mockRule, never()).evaluate(any());
    }

    @Test
    void shouldRejectUnboundedEventTypeAndReferenceFields() {
        NormalizedEventRepository mockRepo = mock(NormalizedEventRepository.class);
        BruteForceDetectionRule mockRule = mock(BruteForceDetectionRule.class);
        EventService service = new EventService(mockRepo, mockRule);

        EventImportRequest request = new EventImportRequest();
        request.setEventTimestamp(Instant.parse("2026-10-23T09:30:00Z"));
        request.setEventType("FAILED LOGIN");
        request.setSourceReferenceId("log-123");

        assertThrows(IllegalArgumentException.class, () -> service.importEvent(request));

        request.setEventType("FAILED_LOGIN");
        request.setSourceReferenceId("x".repeat(129));
        assertThrows(IllegalArgumentException.class, () -> service.importEvent(request));
        verify(mockRepo, never()).save(any());
    }

    @Test
    void shouldThrowExceptionOnMissingRequiredFields() {
        NormalizedEventRepository mockRepo = mock(NormalizedEventRepository.class);
        BruteForceDetectionRule mockRule = mock(BruteForceDetectionRule.class);
        EventService service = new EventService(mockRepo, mockRule);
        EventImportRequest req = new EventImportRequest();
        // Missing timestamp, eventType, etc.
        assertThrows(IllegalArgumentException.class, () -> service.importEvent(req));
        // Ensure nothing was saved or evaluated
        verify(mockRepo, never()).save(any());
        verify(mockRule, never()).evaluate(any());
    }
}