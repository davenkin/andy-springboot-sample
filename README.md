### Introduction

- Device management API for deviceet.

### Tech stack

- Spring Boot 3.5.3
- Spring Data Mongodb 4.5.1
- Spring Kafka 3.3.7
- Kafka Clients 3.8.1

### How to run locally

- First you need run `./start-docker-compose.sh` to start the following local infrastructures:
    - `MongoDB`: localhost:27123
    - `Kafka`: localhost:9123
    - `Kafka UI`: [http://localhost:8123](http://localhost:8123)
    - `Keycloak`: [http://localhost:7123](http://localhost:7123)
    - `Redis`: localhost:6123

- Then to run the application locally, run `./mvnw spring-boot:run`,or run `main()` in `SpringBootWebApplication` in IDE
  directly, after that open
  `http://localhost:5123/about` to check if the application runs successfully.
- To stop local infrastructure, run `./stop-docker-compose.sh` to shut down all docker containers.

### How to run tests

- To run tests, locate them inside IDE and run them directly from there
- We don't do unit testing, instead we only write integration tests
- Tests uses `ci` profile(`application-ci.yaml`) and does not use local docker-compose infrastructures. We are doing
  this because we don't want to rely on docker for running tests, we want developers to clone the code and the tests
  just work without any extra setup. In order to achieve this, we have the following setup:
    - `MongoDB`: uses in memory Mongo provided by `de.flapdoodle.embed`.
    - `Kafka`: disabled as we don't cover event publishing nor event consumer in our tests.
    - `Redis`: uses in memory Redis provided by `todo`.
    - `Keycloak`: disabled as we don't cover authentication and authorization in our tests.
    - All consumed external HTTP services are mocked.

## Architecture Decision Records (ADRs)

This project uses [Architecture Decision Records (ADRs)](https://adr.github.io/) to document important architectural
decisions. Each ADR is stored in the `ADRs` directory and follows a specific format.
