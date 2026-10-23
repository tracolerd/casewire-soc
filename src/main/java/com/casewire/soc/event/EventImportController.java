package com.casewire.soc.event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("/api/events")
public class EventImportController {
    private final EventService eventService;
    public EventImportController(EventService eventService) {
        this.eventService = eventService;
    }
    @PostMapping("/import")
    public ResponseEntity<?> importEvent(@RequestBody EventImportRequest request) {
        try {
            NormalizedEvent saved = eventService.importEvent(request);
            return ResponseEntity.ok(Map.of("status", "success", "id", saved.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
}