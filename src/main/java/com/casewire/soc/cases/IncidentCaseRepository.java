package com.casewire.soc.cases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface IncidentCaseRepository extends JpaRepository<IncidentCase, UUID> {
    Optional<IncidentCase> findByAlertGroupId(UUID alertGroupId);
}