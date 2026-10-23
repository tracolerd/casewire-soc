package com.casewire.soc.cases;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
class IncidentCaseRepositoryTest {
    @Autowired
    private IncidentCaseRepository repository;
    @Test
    void shouldSaveAndRetrieveIncidentCase() {
        // Arrange
        UUID mockAlertId = UUID.randomUUID();
        IncidentCase newCase = new IncidentCase();
        newCase.setTitle("Suspicious Authentication Activity");
        newCase.setAlertGroupId(mockAlertId);
        newCase.setSeverity(CaseSeverity.HIGH);
        // Act
        IncidentCase saved = repository.save(newCase);
        // Assert
        assertThat(saved.getId()).isNotNull();
        IncidentCase retrieved = repository.findByAlertGroupId(mockAlertId).orElse(null);
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getStatus()).isEqualTo(CaseStatus.OPEN); // Default value verification
        assertThat(retrieved.getSeverity()).isEqualTo(CaseSeverity.HIGH);
        assertThat(retrieved.getCreatedAt()).isNotNull();
    }
}