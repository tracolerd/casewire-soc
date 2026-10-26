# Prioritized Engineering Backlog

These candidates reflect the documented deterministic failed-login correlation and alert-to-case investigation workflow.

## Candidate issues

1. **Add authentication and role-based authorization** — Protect API and dashboard operations before connecting the project to a real SIEM or untrusted network.
2. **Remove mock-data fallback from operational states** — Clearly label demo fixtures and ensure API failures cannot be mistaken for live alerts or cases.
3. **Move all sample credentials to safe local configuration** — Remove hard-coded/sample database secrets from tracked config and fail clearly when required environment variables are missing.
4. **Deduplicate repeated alerts deterministically** — Define a correlation key and time window so repeated ingestion does not create uncontrolled duplicate alert records.
5. **Harden event input validation** — Bound payload sizes and validate timestamps, source fields, event types, and malformed records before correlation.
6. **Add tests for the failed-login correlation rule** — Cover time windows, threshold boundaries, duplicate events, out-of-order timestamps, and unrelated source identities.
7. **Enforce case/task/alert ownership integrity** — Prevent links between unrelated records and ensure every mutation preserves valid case relationships.
8. **Add a tamper-evident investigation audit trail** — Record actor, timestamp, action, and relevant references for triage decisions and response-task transitions.
9. **Make frontend API configuration explicit** — Support local/staging/production origins without shipping unsafe defaults or displaying synthetic data as backend truth.
10. **Add reproducible database-backed CI** — Run Java, frontend, migration, and integration checks from a clean PostgreSQL environment.

## Suggested order

Authentication, secret/configuration safety, and live-versus-demo separation are prerequisites to real event data. Next address duplicate handling and validation, then test the detection semantics and investigative history.
