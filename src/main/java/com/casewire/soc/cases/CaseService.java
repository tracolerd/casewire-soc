package com.casewire.soc.cases;
import com.casewire.soc.alert.AlertGroup;
import com.casewire.soc.alert.AlertGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
@Service
public class CaseService {
    private final IncidentCaseRepository caseRepository;
    private final AlertGroupRepository alertRepository;
    public CaseService(IncidentCaseRepository caseRepository, AlertGroupRepository alertRepository) {
        this.caseRepository = caseRepository;
        this.alertRepository = alertRepository;
    }
    @Transactional
    public IncidentCase triageAlertToCase(UUID alertGroupId) {
        // Idempotent check: return existing case if already triaged
        return caseRepository.findByAlertGroupId(alertGroupId)
                .orElseGet(() -> {
                    AlertGroup alert = alertRepository.findById(alertGroupId)
                            .orElseThrow(() -> new IllegalArgumentException("AlertGroup not found"));
                    IncidentCase newCase = new IncidentCase();
                    newCase.setTitle("Investigation: " + alert.getRuleName());
                    newCase.setAlertGroupId(alert.getId());
                    newCase.setSeverity(CaseSeverity.HIGH); // Default triage severity
                    return caseRepository.save(newCase);
                });
    }
    public List<IncidentCase> getAllCases() {
        return caseRepository.findAll();
    }
    @Transactional
    public IncidentCase updateCase(UUID caseId, CaseUpdateRequest request) {
        IncidentCase incident = caseRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("Incident Case not found"));
        if (request.getStatus() != null) incident.setStatus(request.getStatus());
        if (request.getSeverity() != null) incident.setSeverity(request.getSeverity());
        if (request.getOwner() != null) incident.setOwner(request.getOwner());
        return caseRepository.save(incident);
    }
}