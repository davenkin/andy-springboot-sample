### Introduction

- Device management API for deviceet.

### Tech stack

- Spring Boot 3.5.3
- Spring Data Mongodb 4.5.1
- Spring Kafka 3.3.7
- Kafka Clients 3.8.1

### How to run locally

- First you need to setup local infrastructure, run `./start-docker-compose.sh` to start MongoDB, Kafka, Kafka UI and
  Keycloak docker containers.
    - MongoDB: localhost:27123
    - Kafka: localhost:9123
    - Kafka UI: [http://localhost:8123](http://localhost:8123)
    - Keycloak: [http://localhost:7123](http://localhost:7123)
- Then to run the application locally, run `./mvnw spring-boot:run`,or run `main()` in `SpringBootWebApplication` in IDE
  directly, after that open
  `http://localhost:6123/about` to check if the application runs successfully.
- To stop local infrastructure, run `./stop-docker-compose.sh` to shut down all docker containers.
