package com.casewire.soc.cases;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
class AnalystNoteRepositoryTest {
    @Autowired
    private AnalystNoteRepository repository;
    @Test
    void shouldSaveAndRetrieveNotesByCaseId() {
        // Arrange
        UUID caseId = UUID.randomUUID();
        AnalystNote note1 = new AnalystNote();
        note1.setCaseId(caseId);
        note1.setAuthor("analyst_alice");
        note1.setContent("Checked IP reputation. It is clean.");
        AnalystNote note2 = new AnalystNote();
        note2.setCaseId(caseId);
        note2.setAuthor("analyst_bob");
        note2.setContent("Contacted the user. Confirmed it was a typo in password.");
        repository.save(note1);
        repository.save(note2);
        // Act
        List<AnalystNote> retrievedNotes = repository.findByCaseIdOrderByCreatedAtAsc(caseId);
        // Assert
        assertThat(retrievedNotes).hasSize(2);
        assertThat(retrievedNotes.get(0).getAuthor()).isEqualTo("analyst_alice");
        assertThat(retrievedNotes.get(1).getAuthor()).isEqualTo("analyst_bob");
    }
}