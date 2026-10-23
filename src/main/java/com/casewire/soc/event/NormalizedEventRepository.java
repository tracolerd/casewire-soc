package com.casewire.soc.event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
@Repository
public interface NormalizedEventRepository extends JpaRepository<NormalizedEvent, UUID> {
}