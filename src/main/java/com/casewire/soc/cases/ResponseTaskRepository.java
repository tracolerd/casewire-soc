package com.casewire.soc.cases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
@Repository
public interface ResponseTaskRepository extends JpaRepository<ResponseTask, UUID> {
    List<ResponseTask> findByCaseIdOrderByCreatedAtAsc(UUID caseId);
}