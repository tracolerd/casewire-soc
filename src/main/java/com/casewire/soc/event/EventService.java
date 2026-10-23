package com.casewire.soc.event;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class EventService {
    private final NormalizedEventRepository repository;
    public EventService(NormalizedEventRepository repository) {
        this.repository = repository;
    }
    @Transactional
    public NormalizedEvent importEvent(EventImportRequest request) {
        // Fast-fail validations for required fields
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
        return repository.save(event);
    }
}