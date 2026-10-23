package com.casewire.soc.event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
class EventStoreTest {
    @Autowired
    private EventStore eventStore;
    @Autowired
    private NormalizedEventRepository repository;
    @Test
    void shouldReturnEventsWithinTimeWindow() {
        // Arrange
        String targetIp = "10.0.0.5";
        String eventType = "FAILED_LOGIN";
        NormalizedEvent e1 = createEvent(eventType, targetIp, Instant.parse("2026-10-23T09:00:00Z"), "ref-1"); // Too early
        NormalizedEvent e2 = createEvent(eventType, targetIp, Instant.parse("2026-10-23T10:00:00Z"), "ref-2"); // Inside window
        NormalizedEvent e3 = createEvent(eventType, targetIp, Instant.parse("2026-10-23T11:00:00Z"), "ref-3"); // Inside window
        NormalizedEvent e4 = createEvent(eventType, targetIp, Instant.parse("2026-10-23T12:00:00Z"), "ref-4"); // Too late
        NormalizedEvent e5 = createEvent(eventType, "192.168.1.1", Instant.parse("2026-10-23T10:30:00Z"), "ref-5"); // Inside time, wrong IP
        repository.saveAll(List.of(e1, e2, e3, e4, e5));
        Instant start = Instant.parse("2026-10-23T09:30:00Z");
        Instant end = Instant.parse("2026-10-23T11:30:00Z");
        // Act
        List<NormalizedEvent> results = eventStore.findEventsByTypeAndIpWithinTimeWindow(eventType, targetIp, start, end);
        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(NormalizedEvent::getSourceReferenceId).containsExactlyInAnyOrder("ref-2", "ref-3");
    }
    private NormalizedEvent createEvent(String type, String ip, Instant time, String ref) {
        NormalizedEvent e = new NormalizedEvent();
        e.setEventType(type);
        e.setSourceIp(ip);
        e.setEventTimestamp(time);
        e.setSourceReferenceId(ref);
        return e;
    }
}