package com.casewire.soc.event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Repository
public interface NormalizedEventRepository extends JpaRepository<NormalizedEvent, UUID> {
    // Time-window based query for Phase 3 Correlation Engine
    @Query("SELECT e FROM NormalizedEvent e WHERE e.eventType = :eventType AND e.sourceIp = :sourceIp AND e.eventTimestamp BETWEEN :startTime AND :endTime ORDER BY e.eventTimestamp ASC")
    List<NormalizedEvent> findWithinTimeWindow(
            @Param("eventType") String eventType, 
            @Param("sourceIp") String sourceIp, 
            @Param("startTime") Instant startTime, 
            @Param("endTime") Instant endTime);
}