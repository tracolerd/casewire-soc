package com.casewire.soc.cases;
import com.casewire.soc.alert.AlertGroup;
import com.casewire.soc.alert.AlertGroupRepository;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
class CaseServiceTest {
    @Test
    void shouldCreateNewCaseIfNoneExists() {
        IncidentCaseRepository mockCaseRepo = mock(IncidentCaseRepository.class);
        AlertGroupRepository mockAlertRepo = mock(AlertGroupRepository.class);
        AnalystNoteRepository mockNoteRepo = mock(AnalystNoteRepository.class);
        ResponseTaskRepository mockTaskRepo = mock(ResponseTaskRepository.class);
        CaseService service = new CaseService(mockCaseRepo, mockAlertRepo, mockNoteRepo, mockTaskRepo);
        UUID alertId = UUID.randomUUID();
        AlertGroup alert = new AlertGroup();
        alert.setId(alertId);
        alert.setRuleName("Test Rule");
        when(mockCaseRepo.findByAlertGroupId(alertId)).thenReturn(Optional.empty());
        when(mockAlertRepo.findById(alertId)).thenReturn(Optional.of(alert));
        when(mockCaseRepo.save(any(IncidentCase.class))).thenAnswer(i -> i.getArguments()[0]);
        IncidentCase newCase = service.triageAlertToCase(alertId);
        assertNotNull(newCase);
        assertEquals("Investigation: Test Rule", newCase.getTitle());
        assertEquals(CaseSeverity.HIGH, newCase.getSeverity());
        verify(mockCaseRepo, times(1)).save(any(IncidentCase.class));
    }
    @Test
    void shouldUpdateCaseStatusAndOwner() {
        IncidentCaseRepository mockCaseRepo = mock(IncidentCaseRepository.class);
        AlertGroupRepository mockAlertRepo = mock(AlertGroupRepository.class);
        AnalystNoteRepository mockNoteRepo = mock(AnalystNoteRepository.class);
        ResponseTaskRepository mockTaskRepo = mock(ResponseTaskRepository.class);
        CaseService service = new CaseService(mockCaseRepo, mockAlertRepo, mockNoteRepo, mockTaskRepo);
        UUID caseId = UUID.randomUUID();
        IncidentCase existingCase = new IncidentCase();
        existingCase.setId(caseId);
        existingCase.setStatus(CaseStatus.OPEN);
        CaseUpdateRequest req = new CaseUpdateRequest();
        req.setStatus(CaseStatus.IN_PROGRESS);
        req.setOwner("analyst_bob");
        when(mockCaseRepo.findById(caseId)).thenReturn(Optional.of(existingCase));
        when(mockCaseRepo.save(any(IncidentCase.class))).thenAnswer(i -> i.getArguments()[0]);
        IncidentCase updatedCase = service.updateCase(caseId, req);
        assertEquals(CaseStatus.IN_PROGRESS, updatedCase.getStatus());
        assertEquals("analyst_bob", updatedCase.getOwner());
    }
}