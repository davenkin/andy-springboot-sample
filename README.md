### Introduction

- Device management API for deviceet.

### How to run

- run `./start-docker-compose.sh` to start mongo and kafka docker containers
    - MongoDB: localhost:27123
    - Kafka: localhost:9123
    - Kafka UI: [http://localhost:8123](http://localhost:8123)
    - Keycloak: [http://localhost:7123](http://localhost:7123)
- `./mvnw spring-boot:run`,or run `main()` in `SpringBootWebApplication` in IDE directly,then open
  `http://localhost:6123/about` to check if the application runs successfully
- run `./stop-docker-compose.sh` to shut down all docker containers

### Tech stack

- Spring Boot 3.5.3
- Spring Data Mongodb 4.5.1
- Spring Kafka 3.3.7
- Kafka Clients 3.8.1