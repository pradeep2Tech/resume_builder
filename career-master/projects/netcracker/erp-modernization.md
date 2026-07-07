# ERP Modernization

**Company:** [Netcracker](../companies/netcracker.md)  
**Period:** July 2025 — Present  
**Role on project:** System Architect / Technical Lead  
**Team size:** 8

## Problem

Large-scale Netcracker ERP monolith with 100+ tightly coupled modules across Finance, HR, Procurement, Travel, Inventory, Workforce Management, Revenue Planning, Legal, and Workflow — running on legacy Java 8 and OpenShift with limited observability and slow release cycles.

## Solution

- Defined enterprise modernization roadmap and target-state cloud-native architecture
- Quantified infrastructure capacity requirements per modernization wave; automated sizing estimates with scripts
- Domain decomposition, dependency mapping, and impact assessments across workflow engines, approval engines, and integration services
- OpenShift → Kubernetes migration with Docker containerization
- Java 8 → Java 17 upgrade (framework compatibility, dependency resolution, build modernization)
- Observability stack: OpenTelemetry, OpenSearch (migrated from Elasticsearch), Grafana — integrated across ERP applications
- Identity and security: ADFS integration, Keycloak identity provider, RBAC, JWT, OPA policy standards
- MinIO and Vault integration for object storage and secrets
- GitLab CI/CD for standardized deployments

## Impact

- **40%** improvement in engineering delivery capacity (service independence, parallel development, reduced regression)
- **6 critical ERP components** delivered — Customer Delivery Management, Resource Management, HRMS, Travel, Finance, and Workforce — with roadmap for remaining modules
- Infrastructure capacity requirements documented in roadmap; automated sizing scripts for repeatable deployment planning
- Metrics integrated across all applications; centralized logging and distributed tracing established
- ADFS federation and Keycloak-based identity with RBAC, JWT, and OPA policy enforcement

## Tech stack

`Java` `Spring Boot` `Kubernetes` `Docker` `OpenShift` `Oracle` `PostgreSQL` `MongoDB` `OpenSearch` `Grafana` `OpenTelemetry` `GitLab CI/CD` `MinIO` `Vault` `Keycloak` `ADFS` `JWT` `RBAC` `OPA`

## Resume bullets

- Leading modernization of 100+ module Enterprise ERP spanning Finance, HR, Procurement, Travel, Inventory, Workforce, Revenue Planning, Legal, and Workflow domains.
- Defined target-state architecture and transformation strategy evolving tightly coupled monolith into scalable cloud-native platform.
- Led OpenShift-to-Kubernetes migration and Java 8-to-17 modernization across enterprise applications.
- Integrated enterprise observability with OpenTelemetry, OpenSearch, and Grafana; migrated from Elasticsearch with metrics across applications.
- Integrated ADFS; established Keycloak identity provider with RBAC, JWT, and OPA policy standards.
- Defined modernization roadmap with infrastructure capacity sizing; automated estimates with scripts for repeatable deployment planning.
- Delivered 6 ERP modules: Customer Delivery Management, Resource Management, HRMS, Travel, Finance, and Workforce; roadmap for remaining modules.
- Improved engineering delivery capacity by 40% through deployment standardization, service independence, and reduced regression effort.
- Integrated MinIO (S3-compatible) and HashiCorp Vault for object storage and secrets.
- Performed domain decomposition and dependency analysis supporting microservices adoption and long-term platform evolution.
