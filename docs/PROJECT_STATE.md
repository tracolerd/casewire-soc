# Project State: CASEWIRE SOC
**Current Phase:** Phase 2 - Normalized event store and custom queries
**Last Updated:** October 23, 2026
## Mission
To build a SOC case-management demonstrator that normalizes synthetic security events, correlates them via deterministic rules, and manages incident workflows without relying on opaque ML models out of the gate.
## Milestone Status
- [x] Phase 0: Verify toolchain, clone repository, initialize project state.
- [x] Phase 1: Spring Boot scaffold, NormalizedEvent entity, Import API with validation.
- [x] Phase 2 (Part A): EventStore abstraction and Time-Window JPA Query implemented.
- [ ] Phase 2 (Part B): Integration tests for EventStore querying real PostgreSQL DB.
## Known Risks & Limitations
- Initial development uses synthetic logs only. MVP runs locally.
- Real organizational SIEM connections are explicitly out of scope for the MVP.
- Local PostgreSQL instance is used with placeholder credentials for rapid MVP iteration.