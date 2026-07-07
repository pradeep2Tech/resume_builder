# Loyalty & Rewards Platform

**Company:** [Entain (Ivy Comptech)](../companies/entain.md)  
**Period:** September 2018 — June 2025  
**Role on project:** Software Architect / Principal Engineer / Team Lead  
**Clients:** GVC / Entain customers

## Problem

Legacy reward systems with multiple reward types, restrictions, and siloed campaign platforms — limiting scalability, increasing integration effort, and constraining customer engagement across gaming markets.

## Sub-projects

| Initiative | Period | Summary |
|------------|--------|---------|
| Reward System 2.0 | Feb 2022 — Jun 2025 | Cloud-native rewrite of legacy reward services |
| Reward System (v1) | Sep 2019 — Jan 2022 | BAU delivery and scalable reward proposal |
| Promo Hub | Sep 2018 — Aug 2019 | Centralized offers/campaigns comparison platform |
| Loyalty eCommerce | 2015 — 2025 | In-house points-based reward eCommerce app |

## Solution

- **Reward System 2.0:** Golang + Java + Spring Boot + Vert.x; Redis caching (replaced Terracotta); Oracle → CockroachDB via PySpark; NATS messaging
- **Promo Hub:** Unified multiple campaigns and reward systems into single cohesive platform (team of 12)
- **Loyalty eCommerce:** Points-based system incentivizing user activities with redeemable rewards
- HLD/LLD proposals, POCs, and framework benchmarking for optimal scalability

## Impact

- **30%** reduction in development effort; **50%** reduction in integration effort (Reward System 2.0)
- **15%** memory reduction migrating Terracotta → Redis
- **30%** database latency reduction (Oracle → CockroachDB)
- Next-gen loyalty system significantly enhanced retention and engagement
- Promo Hub enabled consolidated offer comparison for end users

## Tech stack

`Golang` `Java` `Spring Boot` `Vert.x` `Hibernate` `Kafka` `NATS` `Redis` `CockroachDB` `Oracle` `PySpark` `JBoss` `Maven`

## Resume bullets

- Led team of 6 engineers delivering cloud-native Reward System 2.0, reducing development effort 30% and integration effort 50%.
- Migrated Oracle to CockroachDB using PySpark; optimized schema and queries for 30% latency reduction.
- Replaced Terracotta with Redis caching, cutting memory usage 15% and improving performance.
- Integrated NATS messaging for efficient service-to-service communication.
- Owned Promo Hub integration unifying multiple campaigns and reward systems (team of 12).
- Developed next-gen loyalty eCommerce platform with points-based rewards driving retention and engagement.
