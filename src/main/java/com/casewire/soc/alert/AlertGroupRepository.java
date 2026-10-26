package com.casewire.soc.alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
@Repository
public interface AlertGroupRepository extends JpaRepository<AlertGroup, UUID> {
    java.util.Optional<AlertGroup>
            findFirstByRuleNameAndGroupingKeyAndTriggeredAtGreaterThanEqualAndTriggeredAtLessThanEqualOrderByTriggeredAtDesc(
                    String ruleName, String groupingKey, java.time.Instant from, java.time.Instant to);
}