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