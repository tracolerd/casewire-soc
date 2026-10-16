# Casewire SOC

**Turn security events into traceable investigations and response workflows.**

Casewire SOC is a planned security operations case-management workspace. It is designed to normalize sample security events, apply transparent correlation rules, and help analysts track cases, timelines, evidence references, and response tasks.

> **Project status:** Initial planning and repository setup. Event processing, correlation rules, UI, and SIEM integrations are not implemented yet.

## Problem

An event stream by itself does not make an investigation. Analysts need context, links between related observations, case ownership, a timeline, evidence, and a record of what was done. Casewire SOC aims to provide a focused workflow for that process using synthetic or authorized data.

## Planned MVP

- Import validated synthetic JSON or CSV events.
- Normalize source fields into a documented internal event schema.
- Apply simple, deterministic correlation rules.
- Create and manage incident cases.
- Record severity, status, assignee, analyst notes, and response checklist items.
- Build a timeline from observed events and documented analyst actions.
- Track evidence metadata and optionally verify file integrity hashes.
- Export an investigation summary for review.

Future integration with SIEM products is an adapter goal, not a current capability.

## Proposed architecture

~~~mermaid
flowchart TD
    A[Synthetic or Authorized Events] --> B[Validation and Normalization]
    B --> C[Normalized Event Store]
    C --> D[Deterministic Correlation Rules]
    D --> E[Alert Grouping]
    E --> F[Incident Case]
    F --> G[Timeline, Evidence, Response Tasks]
    G --> H[Investigation Report]
~~~

The event normalizer, correlation engine, case workflow, and presentation layer should be distinct so that rule behavior is testable without a live SIEM.

## Proposed technology

| Area | Planned choice |
|---|---|
| Backend | Java and Spring Boot |
| Frontend | React and TypeScript |
| Database | PostgreSQL |
| API | REST |
| Test framework | JUnit and suitable frontend test tooling |
| Initial data | Synthetic JSON/CSV fixtures |
| Future integrations | Explicitly tested import adapters for selected Splunk or LogRhythm formats |

## Correlation design

Start with understandable rules: repeated authentication failures for a single synthetic identity within a time window, or related events sharing a documented host and time range. Each rule should declare required fields, window semantics, grouping keys, and known limitations.

Correlation is a prioritization aid. It does not independently prove that an incident occurred. Findings should link to contributing events and explain why they were grouped.

## Evidence model

For each case, record source event identifiers, timestamps, import provenance, analyst actions, and evidence references. A SHA-256 digest can help detect whether a file changed after hashing, but does not prove that the file is authentic or that its source was trustworthy. This limitation must remain visible in documentation.

## Development roadmap

1. Define the normalized event schema and case lifecycle.
2. Implement Spring Boot domain services, persistence, and JUnit tests.
3. Add synthetic event fixtures and deterministic correlation rules.
4. Implement case management and investigation timelines.
5. Add audit events, evidence integrity checks, and report export.
6. Add authentication and authorization with negative tests.
7. Build and validate one external log import adapter.
8. Add CI, deployment guidance, and operational limits.

## Testing strategy

- Valid and malformed event fixtures.
- Missing required field and timestamp normalization tests.
- Time-window boundary and duplicate-event cases.
- Correlation positive and negative tests.
- Case state-transition and authorization tests.
- Evidence hash verification tests.
- Report redaction and export tests.
- Integration tests for database consistency.

## Security and privacy

Logs may contain usernames, IP addresses, internal hostnames, and other sensitive information. Use synthetic or sanitized examples, restrict access to case data, and redact secrets from logs and exported reports. The initial project should not require access to a live organizational SIEM.

## Current status

This repository contains the project brief and MIT license. No operational SOC, live event pipeline, verified detection logic, or SIEM integration has been delivered yet.

## Author & Development

**Project author and solo developer:** [Nurul Faiyaz](https://github.com/tracolerd).

This project is independently developed and maintained by Nurul Faiyaz. AI-assisted tools may be used as part of the development workflow; the project author remains responsible for technical decisions, code review, validation, and maintenance.

## License

MIT. See [LICENSE](LICENSE).
