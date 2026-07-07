# DAP Restructure

**Company:** [Envestnet Yodlee](../companies/envestnet-yodlee.md)  
**Period:** January 2015 — November 2015  
**Role on project:** Technical Lead  
**Team size:** 4  
**Client:** Yodlee customers

## Problem

The existing Data Aggregation Platform (DAP) architecture had coupling and scalability issues. Web crawling via Selenium needed a full component rewrite to deliver independent, maintainable modules.

## Solution

- Gathered requirements from multiple stakeholders and analyzed architectural pain points
- End-to-end application redesign with decoupled independent modules
- Custom algorithms for graph-related crawling problems
- Custom static rule engine for data aggregation logic
- Spring 4, Hibernate 5, Jackson; Maven, JBoss, JSON/Spring Boot patterns
- Unit testing for all business requirements

## Impact

- Delivered quality product with fully decoupled components
- Resolved legacy architecture problems through modular rewrite
- Foundation for next-generation Yodlee data aggregation platform

## Tech stack

`Java` `Spring 4` `Hibernate 5` `Jackson` `Spring Boot` `Maven` `JBoss` `Selenium` `Custom algorithms` `Static rule engine`

## Design patterns & techniques

Custom static rule engine · Graph algorithms · Modular decoupling

## Resume bullets

- Led technical redesign of DAP data aggregation platform, decoupling monolithic components into independent modules.
- Designed end-to-end architecture and custom static rule engine for web crawling via Selenium.
- Developed custom graph algorithms solving complex financial data container integration challenges.
- Delivered full business requirements with unit-tested, production-quality modules.

## Related

- Preceded by [DAP Aggregation Platform](dap-aggregation.md)
- Followed Yodlee [PFM](pfm.md) and [Issue Analyzer](issue-analyzer.md) programs
