## Introduction

- This is a sample Spring Boot project for building Microservices with the following features:
    - Data persistence with MongoDB
    - Messaging using Kafka
    - Cache using Redis
    - API documentation using [Springdoc](https://springdoc.org/)
    - Data migration using [Mongock](https://mongock.io/)
    - Architecture validation using [ArchUnit](https://www.archunit.org/)
    - Distributed lock for scheduled jobs using [Shedlock](https://github.com/lukas-krecan/ShedLock)
    - Standardized [folder structure](./ADRs/005_project_structure.md) with business first approach
    - Standardized pagination support with [PageableRequest](./src/main/java/deviceet/common/util/PageableRequest.java)
      and [PagedResponse](./src/main/java/deviceet/common/util/PagedResponse.java)
    - Builtin [Snowflake ID generator](./src/main/java/deviceet/common/util/SnowflakeIdGenerator.java)
    - Domain event modeling based on [DomainEvent](./src/main/java/deviceet/common/event/DomainEvent.java)
    - [Domain event publishing](./ADRs/008_domain_event_publishing.md)
      using [Transactional Outbox](https://microservices.io/patterns/data/transactional-outbox.html) pattern
    - [Event consuming](./ADRs/009_event_consuming.md) mechanism with idempotency support
    - Standardized [exception handling](./ADRs/012_exception_handling.md)
    - Lightweight [Command Query Responsibility Segregation (CQRS)](./ADRs/004_use_cqrs.md) support
    - Domain modeling using [Domain Driven Design (DDD)](./ADRs/003_use_ddd.md)
    - Standardized [request process flow](./ADRs/006_request_process_flow.md)
    - Standardized [object implementation pattern](./ADRs/007_object_implementation_patterns.md)

## Tech stack

- Java 17
- Spring Boot 3.5.3
- Spring Data Mongodb 4.5.1
- Spring Data Redis 3.5.0
- Spring Kafka 3.3.7

## How to run locally

- First run `./start-docker-compose.sh` to start the following infrastructures:
    - `MongoDB`: localhost:27123
    - `Kafka`: localhost:9123
    - `Kafka UI`: [http://localhost:8123](http://localhost:8123)
    - `Keycloak`: [http://localhost:7123](http://localhost:7123)
    - `Redis`: localhost:6123
- Run the application in one of the following ways:
    - `./run-local.sh`: this starts the application with debug port on 5005, assuming that docker-compose is already
      up running.
    - `./clear-and-run-local.sh`: this starts the application with debug port on 5005, it also automatically starts
      docker-compose by first removing existing containers and their data if any.
    - Run `main` in  `SpringBootWebApplication`, assuming that docker-compose is already up running.
- Open [http://localhost:5123/about](http://localhost:5123/about) to check if the application runs
  successfully.
- To stop docker-compose and delete data, run `./stop-docker-compose.sh`.

## How to build

- Run `./build.sh` to build the project locally.

## How to run tests

- We do both integration testing and unit testing with a preference on integration testing
- To run tests, locate them inside IDE and run them directly from there.
- We have a [testing strategy](./ADRs/010_testing_strategy.md), please read it before writing any tests

## Architecture Decision Records (ADRs)

This project uses [Architecture Decision Records (ADRs)](https://adr.github.io/) to document important architectural
decisions. ADRs are stored in the `ADRs` directory and follow a [specific format](ADRs/000_what_is_adr.md).

## Sample implementation code

- The `src/test/java/deviceet/sample` folder contains various common coding practices that should be followed when
  writing your own code. Please refer
  to [sample code introduction](src/test/java/deviceet/sample/sample-code-introduction.md) for more detail.

## Top level business entities

| Business Entity   | Chinese | Abbreviation | Description                                                                                                                           |
|-------------------|---------|--------------|---------------------------------------------------------------------------------------------------------------------------------------|
| Equipment         | 装备      |              | Sample top level business entity that serves as a reference for consistent coding practice. An Equipment has many MaintenanceRecords. |
| MaintenanceRecord | 装备维护记录  |              | Another sample top level business entity. Multiple MaintenanceRecords can be created for a single  Equipment.                         |

