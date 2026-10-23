package com.casewire.soc.event;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
@Service
public class EventStore {
    private final NormalizedEventRepository repository;
    public EventStore(NormalizedEventRepository repository) {
        this.repository = repository;
    }
    public List<NormalizedEvent> findEventsByTypeAndIpWithinTimeWindow(String eventType, String sourceIp, Instant startTime, Instant endTime) {
        if (eventType == null || sourceIp == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException("All parameters must be provided for time-window query");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("startTime cannot be after endTime");
        }
        return repository.findWithinTimeWindow(eventType, sourceIp, startTime, endTime);
    }
}