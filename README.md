### Introduction

- Backend template for UDS platform solutions.

### How to run

- run `./start-docker-compose.sh` to start mongo and kafka docker containers
    - MongoDB: localhost:27017
    - Kafka: localhost:9092
    - Kafka UI: [http://localhost:8070](http://localhost:8070)
- `./mvnw spring-boot:run` and open `http://localhost:8080/about`
- Or run `main()` in `SpringBootWebApplication` in IDE directly
- Run all tests
- run `./stop-docker-compose.sh` to shut down all docker containers

### Tech stack

- Spring Boot 3.5.3
- Spring Data Mongodb 4.5.1
- Spring Kafka 3.3.7
- Kafka Clients 3.8.1