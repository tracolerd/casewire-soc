# Project State: CASEWIRE SOC
**Current Phase:** Phase 3 - Correlation rule engine with positive/negative fixtures (COMPLETED)
**Last Updated:** October 23, 2026
## Mission
To build a SOC case-management demonstrator that normalizes synthetic security events, correlates them via deterministic rules, and manages incident workflows without relying on opaque ML models out of the gate.
## Milestone Status
- [x] Phase 0: Verify toolchain, clone repository, initialize project state.
- [x] Phase 1: Spring Boot scaffold, NormalizedEvent entity, Import API with validation.
- [x] Phase 2: EventStore abstraction and Integration tests.
- [x] Phase 3 (Part A): AlertGroup entity and BruteForceDetectionRule implemented with threshold logic.
- [x] Phase 3 (Part B): Tie detection rule to EventService ingestion pipeline.
- [ ] Phase 4: Incident case lifecycle, mapping AlertGroups to Cases.
## Known Risks & Limitations
- Initial development uses synthetic logs only. MVP runs locally.
- Real organizational SIEM connections are explicitly out of scope for the MVP.
- Rule evaluation is currently synchronous. May need async scaling in a real large-scale deployment, but sufficient for demonstrator.