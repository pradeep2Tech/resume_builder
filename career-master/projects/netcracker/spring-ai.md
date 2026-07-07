# Spring AI & AI-Assisted Engineering

**Company:** [Netcracker](../companies/netcracker.md)  
**Period:** 2024 — Present  
**Role on project:** System Architect / technical lead  
**Initiative:** ADLC (Application Development Life Cycle)

## Problem

Distributed engineering teams lacked automated visibility into sprint hygiene before daily scrums, and solution architects spent significant time triaging Jira tickets requiring architectural input.

## Sub-projects

| Project | Summary |
|---------|---------|
| **Scrum Agent** | AI agent that fetches sprint data across teams, posts morning updates to individual Webex groups, highlights missing comments and overdue items, and prompts ticket closure before scrum calls |
| **Solution Analyzer** | AI agent that reads Jira tickets involving solution architects and generates proposed solutions |

## Solution

### Scrum Agent

- Fetches sprint status across all teams each morning
- Publishes personalized updates to individual team Webex group channels
- Highlights team members who have not updated comments or missed due dates
- Sends reminders before scrum calls; prompts users to update comments or close tickets when forgotten
- Built as part of the ADLC automation initiative using Spring AI and custom AI agents

### Solution Analyzer

- Integrates with Jira to identify tickets where a solution architect is involved
- Analyzes ticket context and generates solution recommendations for architect review
- Reduces manual triage effort and accelerates architectural response on open items

### Enablement

- Delivered enterprise knowledge-sharing sessions on Spring AI, LLM evaluation, and AI-assisted development (Claude, Cursor, custom agents)
- Integrated AI agent patterns into architecture governance and engineering enablement

## Impact

- Improved sprint readiness and ticket hygiene before daily scrum calls across teams
- Reduced manual follow-up on overdue comments, due dates, and unclosed tickets
- Accelerated solution architect triage on Jira items requiring architectural input
- Best Presenter recognition for Microservices Architecture sessions at Netcracker Hyderabad

## Tech stack

`Spring AI` `Java` `Spring Boot` `Jira` `Webex` `Custom AI Agents` `LLM evaluation` `Claude` `Cursor`

## Resume bullets

- Built **Scrum Agent** (ADLC initiative): fetches team sprint data, posts morning updates to Webex groups, highlights missing comments and due-date misses, and prompts ticket updates/closure before scrum calls.
- Built **Solution Analyzer**: reads Jira tickets involving solution architects and generates proposed solutions, reducing manual triage effort.
- Delivered enterprise sessions on Spring AI, LLM evaluation, and AI-assisted development (Claude, Cursor, custom AI agents).

## Related

- [ERP Modernization](erp-modernization.md) · [Platform Engineering](platform-engineering.md)
