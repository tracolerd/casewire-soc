# Architecture Decision Records (ADRs)
## ADR 001: Documentation & State Management
- **Date:** 2026-10-26
- **Context:** We need an isolated way for the AI engineering partner to track project state and architectural choices without modifying the root README.md.
- **Decision:** Use docs/PROJECT_STATE.md for task tracking/status and docs/DECISIONS.md for architecture decisions. The root README remains untouched.
## ADR 002: Development Environment & Toolchain
- **Date:** 2026-10-26
- **Context:** We need a consistent, predefined development environment.
- **Decision:** Development environment is Windows 10 Pro using PowerShell exclusively. Toolchain: Java 26, PostgreSQL 16, Node 24.
