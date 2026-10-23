package com.casewire.soc.alert;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "alert_groups")
public class AlertGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String ruleName;
    @Column(nullable = false)
    private String groupingKey;
    @Column(nullable = false)
    private Instant triggeredAt;
    private String description;
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public String getGroupingKey() { return groupingKey; }
    public void setGroupingKey(String groupingKey) { this.groupingKey = groupingKey; }
    public Instant getTriggeredAt() { return triggeredAt; }
    public void setTriggeredAt(Instant triggeredAt) { this.triggeredAt = triggeredAt; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}