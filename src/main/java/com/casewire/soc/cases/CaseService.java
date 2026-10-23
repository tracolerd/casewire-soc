package com.casewire.soc.cases;
import com.casewire.soc.alert.AlertGroup;
import com.casewire.soc.alert.AlertGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Service
public class CaseService {
    private final IncidentCaseRepository caseRepository;
    private final AlertGroupRepository alertRepository;
    private final AnalystNoteRepository noteRepository;
    private final ResponseTaskRepository taskRepository;
    public CaseService(IncidentCaseRepository caseRepository, 
                       AlertGroupRepository alertRepository,
                       AnalystNoteRepository noteRepository,
                       ResponseTaskRepository taskRepository) {
        this.caseRepository = caseRepository;
        this.alertRepository = alertRepository;
        this.noteRepository = noteRepository;
        this.taskRepository = taskRepository;
    }
    @Transactional
    public IncidentCase triageAlertToCase(UUID alertGroupId) {
        return caseRepository.findByAlertGroupId(alertGroupId)
                .orElseGet(() -> {
                    AlertGroup alert = alertRepository.findById(alertGroupId)
                            .orElseThrow(() -> new IllegalArgumentException("AlertGroup not found"));
                    IncidentCase newCase = new IncidentCase();
                    newCase.setTitle("Investigation: " + alert.getRuleName());
                    newCase.setAlertGroupId(alert.getId());
                    newCase.setSeverity(CaseSeverity.HIGH);
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
    @Transactional
    public AnalystNote addNoteToCase(UUID caseId, NoteRequest request) {
        if (!caseRepository.existsById(caseId)) throw new IllegalArgumentException("Case not found");
        if (request.getAuthor() == null || request.getContent() == null) throw new IllegalArgumentException("Author and content required");
        AnalystNote note = new AnalystNote();
        note.setCaseId(caseId);
        note.setAuthor(request.getAuthor());
        note.setContent(request.getContent());
        return noteRepository.save(note);
    }
    @Transactional
    public ResponseTask addTaskToCase(UUID caseId, TaskRequest request) {
        if (!caseRepository.existsById(caseId)) throw new IllegalArgumentException("Case not found");
        if (request.getTaskName() == null || request.getTaskName().isBlank()) throw new IllegalArgumentException("Task name required");
        ResponseTask task = new ResponseTask();
        task.setCaseId(caseId);
        task.setTaskName(request.getTaskName());
        return taskRepository.save(task);
    }
    @Transactional
    public ResponseTask completeTask(UUID taskId) {
        ResponseTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setCompleted(true);
        task.setCompletedAt(Instant.now());
        return taskRepository.save(task);
    }
    public Map<String, Object> getCaseTimeline(UUID caseId) {
        if (!caseRepository.existsById(caseId)) throw new IllegalArgumentException("Case not found");
        List<AnalystNote> notes = noteRepository.findByCaseIdOrderByCreatedAtAsc(caseId);
        List<ResponseTask> tasks = taskRepository.findByCaseIdOrderByCreatedAtAsc(caseId);
        return Map.of(
            "notes", notes,
            "tasks", tasks
        );
    }
}