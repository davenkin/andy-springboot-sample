## Introduction

- This is a sample Spring Boot project for building Microservices with an emphasis on the following areas:
    - [Command Query Responsibility Segregation (CQRS)](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/apply-simplified-microservice-cqrs-ddd-patterns)
    - [Event Driven Architecture (EDA)](https://microservices.io/patterns/data/event-driven-architecture.html)
    - [Domain Driven Design (DDD)](https://martinfowler.com/bliki/DomainDrivenDesign.html)

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

- We do both integration testing and unit testing, but we prefer integration testing
- To run tests, locate them inside IDE and run them directly from there.
- We have a [Testing Strategy](./ADRs/010_testing_strategy.md), please read it before writing any tests

## Architecture Decision Records (ADRs)

This project uses [Architecture Decision Records (ADRs)](https://adr.github.io/) to document important architectural
decisions. Each ADR is stored in the `ADRs` directory and follows a specific format. Please refer
to [What is ADR](ADRs/000_what_is_adr.md) for more detail.

## Sample implementation code

- The `src/test/java/deviceet/sample` folder contains various common coding practices that should be followed when
  writing your own code. Please refer
  to [sample code introduction](src/test/java/deviceet/sample/sample-code-introduction.md) for more detail.

## Top level business entities

| Business Entity   | Chinese | Abbreviation | Description                                                                                                                          |
|-------------------|---------|--------------|--------------------------------------------------------------------------------------------------------------------------------------|
| Equipment         | 装备      |              | Sample top level business entity that serves as a reference for consitent coding practice. An Equipment has many MaintenanceRecords. |
| MaintenanceRecord | 装备维护记录  |              | Another sample top level business entity. Multiple MaintenanceRecords can be created for a single  Equipment.                        |

