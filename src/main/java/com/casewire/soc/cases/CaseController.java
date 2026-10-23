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
        try {
            IncidentCase incident = caseService.triageAlertToCase(alertId);
            return ResponseEntity.ok(incident);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllCases() {
        return ResponseEntity.ok(caseService.getAllCases());
    }
    @PatchMapping("/{caseId}")
    public ResponseEntity<?> updateCase(@PathVariable UUID caseId, @RequestBody CaseUpdateRequest request) {
        try {
            IncidentCase updated = caseService.updateCase(caseId, request);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}