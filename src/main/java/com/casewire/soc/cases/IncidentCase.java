package com.casewire.soc.cases;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "incident_cases")
public class IncidentCase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CaseStatus status = CaseStatus.OPEN;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CaseSeverity severity = CaseSeverity.MEDIUM;
    private String owner; // Username of the assigned analyst
    @Column(name = "alert_group_id", nullable = false, unique = true)
    private UUID alertGroupId;
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
    public IncidentCase() {}
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public CaseStatus getStatus() { return status; }
    public void setStatus(CaseStatus status) { this.status = status; }
    public CaseSeverity getSeverity() { return severity; }
    public void setSeverity(CaseSeverity severity) { this.severity = severity; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public UUID getAlertGroupId() { return alertGroupId; }
    public void setAlertGroupId(UUID alertGroupId) { this.alertGroupId = alertGroupId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}