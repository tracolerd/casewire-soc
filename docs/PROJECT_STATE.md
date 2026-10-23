# Project State: CASEWIRE SOC
**Current Phase:** Phase 1 - Entity modeling and import validation
**Last Updated:** October 23, 2026
## Mission
To build a SOC case-management demonstrator that normalizes synthetic security events, correlates them via deterministic rules, and manages incident workflows without relying on opaque ML models out of the gate.
## Milestone Status
- [x] Phase 0: Verify toolchain, clone repository, initialize project state.
- [x] Phase 1 (Part A): Spring Boot scaffold, database config, local Maven setup.
- [x] Phase 1 (Part B): NormalizedEvent core entity & repository created, DB connected.
- [ ] Phase 1 (Part C): Event import REST API and JSON payload validation.
## Known Risks & Limitations
- Initial development uses synthetic logs only. MVP runs locally.
- Real organizational SIEM connections are explicitly out of scope for the MVP.
- Local PostgreSQL instance is used with placeholder credentials for rapid MVP iteration.