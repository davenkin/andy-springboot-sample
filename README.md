## Introduction

- This is a sample Spring Boot project for building Microservice APIs with an emphasis on the following areas:
    - [Command Query Responsibility Segregation](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/apply-simplified-microservice-cqrs-ddd-patterns) (
      CQRS)
    - [Event Driven Architecture](https://microservices.io/patterns/data/event-driven-architecture.html) (EDA)
    - [Domain Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html) (DDD)
    - [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

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
    - Run `./run-local.sh`: this starts the application with debug port on 5005, assuming that docker-compose is already
      up running.
    - Run `./clear-and-run-local.sh`: this starts the application with debug port on 5005, it also automatically starts
      docker-compose by first removing existing docker contains if any, and also deletes all their data.
    - Run `main` in  `SpringBootWebApplication`, assuming that docker-compose is already up running.
- After that open [http://localhost:5123/about](http://localhost:5123/about) to check if the application runs
  successfully.
- To stop local docker compose and delete data volume, run `./stop-docker-compose.sh`.

## How to build

- Run `./build.sh` to build the project locally.

## How to run tests

- To run tests, locate them inside IDE and run them directly from there.
- We do both unit testing and integration testing.
- For unit testing, we mainly test classes under `domain` package.
- For integration testing, the following types of classes are tested:
    - CommandService (
      e.g. [EquipmentCommandServiceIntegrationTest](src/test/java/deviceet/sample/equipment/command/EquipmentCommandServiceIntegrationTest.java))
    - QueryService (
      e.g. [EquipmentQueryServiceIntegrationTest](src/test/java/deviceet/sample/equipment/query/EquipmentQueryServiceIntegrationTest.java))
    - EventHandler (
      e.g. [EquipmentDeletedEventEventHandlerIntegrationTest](src/test/java/deviceet/sample/equipment/eventhandler/EquipmentDeletedEventEventHandlerIntegrationTest.java))
    - Job (
      e.g. [RemoveOldMaintenanceRecordsJobIntegrationTest](src/test/java/deviceet/sample/maintenance/job/RemoveOldMaintenanceRecordsJobIntegrationTest.java))
- For integration testing, the following types of classes are NOT tested:
    - Repository: they are already covered in other types of integration tests
    - Controller: Controllers are very thin but requires a heavy set up for testing, so we decided not to test
- Integration test uses a Spring profile named `it`([application-it.yaml](src/test/resources/application-it.yaml)) for
  its own configuration. Integration tests
  do
  not use local
  docker-compose infrastructures as we don't want to rely on docker for running tests. Instead, we want to ensure
  developers to
  clone the code and the tests
  just work without any extra setup. In order to achieve this, we have the following setup for integration tests:
    - `MongoDB`: uses in memory Mongo provided by `de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring3x`.
    - `Kafka`: disabled as we don't cover event publishing nor event consumer in our tests.
    - `Redis`: uses an embedded redis server `com.github.codemonstur:embedded-redis`.
    - `Keycloak`: disabled as we don't cover authentication in our tests.
    - All consumed external HTTP services are mocked.

## Architecture Decision Records (ADRs)

This project uses [Architecture Decision Records (ADRs)](https://adr.github.io/) to document important architectural
decisions. Each ADR is stored in the `ADRs` directory and follows a specific format. Please refer
to [What is ADR](ADRs/000_what_is_ADR.md) for more detail.

## Sample code for consistent coding practices

- The `src/test/java/deviceet/sample` folder contains various common coding practices that should be followed when
  implementing your own features. Please refer
  to [sample code introduction](src/test/java/deviceet/sample/sample-code-introduction.md) for more detail.

## Top level business entities

| Business Entity   | Chinese | Abbreviation | Description                                                                                                                          |
|-------------------|---------|--------------|--------------------------------------------------------------------------------------------------------------------------------------|
| Equipment         | 装备      |              | Sample top level business entity that serves as a reference for consitent coding practice. An Equipment has many MaintenanceRecords. |
| MaintenanceRecord | 装备维护记录  |              | Another sample top level business entity. Multiple MaintenanceRecords can be created for a single  Equipment.                        |

