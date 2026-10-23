package com.casewire.soc.cases;
public class CaseUpdateRequest {
    private CaseStatus status;
    private CaseSeverity severity;
    private String owner;
    public CaseStatus getStatus() { return status; }
    public void setStatus(CaseStatus status) { this.status = status; }
    public CaseSeverity getSeverity() { return severity; }
    public void setSeverity(CaseSeverity severity) { this.severity = severity; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}