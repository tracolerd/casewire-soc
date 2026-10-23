package com.casewire.soc.event;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
class EventImportControllerTest {
    @Test
    void shouldImportValidEvent() {
        // Arrange
        EventService mockService = mock(EventService.class);
        EventImportController controller = new EventImportController(mockService);
        EventImportRequest request = new EventImportRequest();
        request.setEventTimestamp(Instant.now());
        request.setEventType("FAILED_LOGIN");
        request.setSourceReferenceId("log-12345");
        NormalizedEvent savedEvent = new NormalizedEvent();
        savedEvent.setId(UUID.randomUUID());
        when(mockService.importEvent(any(EventImportRequest.class))).thenReturn(savedEvent);
        // Act
        ResponseEntity<?> response = controller.importEvent(request);
        // Assert
        assertEquals(200, response.getStatusCode().value());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("success", body.get("status"));
        assertNotNull(body.get("id"));
    }
    @Test
    void shouldRejectEventWithoutRequiredFields() {
        // Arrange
        EventService mockService = mock(EventService.class);
        EventImportController controller = new EventImportController(mockService);
        EventImportRequest request = new EventImportRequest();
        when(mockService.importEvent(any(EventImportRequest.class)))
                .thenThrow(new IllegalArgumentException("eventType is required"));
        // Act
        ResponseEntity<?> response = controller.importEvent(request);
        // Assert
        assertEquals(400, response.getStatusCode().value());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("error", body.get("status"));
        assertEquals("eventType is required", body.get("message"));
    }
}