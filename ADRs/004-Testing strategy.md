# Testing strategy

## Context

Backend developers usually write both unit tests and integration tests. According to testing pyramid, unit tests constitute the base of the
pyramid, while integration tests are at a level higher. The goal is to have a large number of unit tests and a smaller number of integration
tests. But in our case, we find that unit tests can be too fragile and require frequent updates when the code changes, so we prefer
integration tests.

## Decision

We choose to **only write integration tests** as unit tests are subject to change when the code changes, which can lead to a lot of
maintenance work. Integration tests are more stable.

Also, we choose to majorly test the following types of classes:

- CommandService
- DomainEventHandler
- Job

## Implementation

When writing integration tests, follow the below guidelines:

- Use `@SpringBootTest` annotation to load the entire application context, this is already been done in `BaseTest`, so you only need to
  extend `BaseTest` and no need to annotate you testing class with `@SpringBootTest` again.
- Use `@Autowire` to get an instance of the class under test and then call its methods directly.
- No need to mock dependencies as the whole application context is up running, except for accessing external APIs.
- Domain events are configured to not be published to Kafka as asynchronous testing can be unstable. In order to verify the publishing of
  domain events, you can just verify the
  event exists in database(we are using the [transactional outbox pattern](https://microservices.io/patterns/data/transactional-outbox.html)
  so domain events will firstly be persisted into database then publish to Kafka using another thread).
- Authentication and authorization are not covered in integration tests, let's cover that in manual testing.