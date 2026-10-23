# Project State: CASEWIRE SOC
**Current Phase:** Phase 4 - Incident case lifecycle (COMPLETED)
**Last Updated:** October 24, 2026
## Mission
To build a SOC case-management demonstrator that normalizes synthetic security events, correlates them via deterministic rules, and manages incident workflows without relying on opaque ML models out of the gate.
## Milestone Status
- [x] Phase 0 to Phase 3: Completed.
- [x] Phase 4 (Part A): IncidentCase entity, Enums (Status/Severity), and Repository created.
- [x] Phase 4 (Part B): Alert-to-Case Triage Service and REST APIs for case management.
- [x] Phase 4 (Part C): Case Timeline, Notes, Response Tasks implemented.
- [ ] Phase 5: React + TypeScript Frontend (Setup & Case Dashboard).
## Known Risks & Limitations
- Initial development uses synthetic logs only. MVP runs locally.
- Real organizational SIEM connections are explicitly out of scope for the MVP.