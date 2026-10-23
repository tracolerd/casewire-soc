package com.casewire.soc.cases;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;
@RestController
@RequestMapping("/api/cases")
public class CaseController {
    private final CaseService caseService;
    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }
    @PostMapping("/triage/{alertId}")
    public ResponseEntity<?> triageAlert(@PathVariable UUID alertId) {
        try { return ResponseEntity.ok(caseService.triageAlertToCase(alertId)); } 
        catch (IllegalArgumentException e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }
    @GetMapping
    public ResponseEntity<?> getAllCases() {
        return ResponseEntity.ok(caseService.getAllCases());
    }
    @PatchMapping("/{caseId}")
    public ResponseEntity<?> updateCase(@PathVariable UUID caseId, @RequestBody CaseUpdateRequest request) {
        try { return ResponseEntity.ok(caseService.updateCase(caseId, request)); } 
        catch (IllegalArgumentException e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }
    @PostMapping("/{caseId}/notes")
    public ResponseEntity<?> addNote(@PathVariable UUID caseId, @RequestBody NoteRequest request) {
        try { return ResponseEntity.ok(caseService.addNoteToCase(caseId, request)); } 
        catch (IllegalArgumentException e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }
    @PostMapping("/{caseId}/tasks")
    public ResponseEntity<?> addTask(@PathVariable UUID caseId, @RequestBody TaskRequest request) {
        try { return ResponseEntity.ok(caseService.addTaskToCase(caseId, request)); } 
        catch (IllegalArgumentException e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }
    @PatchMapping("/tasks/{taskId}/complete")
    public ResponseEntity<?> completeTask(@PathVariable UUID taskId) {
        try { return ResponseEntity.ok(caseService.completeTask(taskId)); } 
        catch (IllegalArgumentException e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }
    @GetMapping("/{caseId}/timeline")
    public ResponseEntity<?> getTimeline(@PathVariable UUID caseId) {
        try { return ResponseEntity.ok(caseService.getCaseTimeline(caseId)); } 
        catch (IllegalArgumentException e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }
}