# Project structure

## Context

When structure software projects, there are mainly 2 approaches:

- Structure by technical layers first: create technical layer packages first, such as `domain`, `service`,
  `infrastructure`, then put
  business entities into these layers.
- Structure by business first: create business packages first, then put technical layers inside these packages.

## Decision

We choose "**business first, technical layers second**" approach as the project structure, which means we organize the
code by business entities([Aggregate Root](https://martinfowler.com/bliki/DDD_Aggregate.html)) first, and then by
technical layers.

This approach is more intuitive and easier to understand, as it allows developers to focus on the business first(Quotes
has it that "
Software exists for solving problems for a specific business domain."). Developers can easily get an overall idea of
what this application
does
by a simple glimpse at the business packages.

## Implementation

When implementing, keep the folder structure as flat as possible. The aggregate root is at the highest level under
`business` package, then followed by other
technical layers, use the following structure:

The `1` in `(class:1)` indicates there can be only one class under a top level package, `(class:N)` for multiple.

- `Aggregate Root`(folder:1): The top level package, an aggregate root represents a major business entity(e.g.
  `equipment`).
    - `command`(folder:1): For sending commands to the application, "command" represents the "C"
      in [CQRS](https://microservices.io/patterns/data/cqrs.html).
        - `XxxCommandService`(class:1): The
          facade [application service](https://ddd-practitioners.com/home/glossary/application-service/)
          class for commands, should end with "CommandService".
        - `XxxCommand`(class:N): Represent a single command, it contains the data that you want to send to the
          application, should end
          with "Command".
    - `domain`(folder:1): Contains all the domain models.
        - `Xxx`(class:N): Domain objects.
        - `XxxRepository`(class:1): Repository interface for persisting aggregate root, should end with "Repository",
          it's implementation
          class is in `infrastructure` folder. Please be noted that repository is per aggregate root, namely only
          aggregate root can have
          repositories, but not all domain objects.
        - `XxxFactory`(class:1): Factory class for creating the aggregate root, should end with "Factory". The creation
          of entities
          should be explicit, so always use factories to create them but not use constructors directly. Normally the
          factory first do some
          business validations then call constructor to create the aggregate root object.
        - `XxxDomainService`(class:N): A [domain service](https://ddd-practitioners.com/home/glossary/domain-service/)
          class, should end
          with "DomainService", domain services should be your last resort when business logic cannot fit into other
          domain objects.
        - `event`(folder:1): This folder contains all the domain event classes that are raised by the aggregate root.
            - XxxEvent(class:N): Domain event class, should end with "Event", it represents a significant change in the
              state of the
              aggregate root, and should be raised by the aggregate root when its state changes. The naming convention
              is `[Aggregate Root Name] + [Passive form of verbs] + Event`, e.g. `UserCreatedEvent`.
        - `task`(folder:1): Contains tasks.
            - `XxxTask`(class:N): A task represents a standalone single operation, should end with "Task". Tasks are
              usually called from jobs and event handlers.
    - `eventhandler`(folder:1): Contains all the event handler classes that results in updates on the aggregate root.
        - `XxxEventHandler`(class): Event handler class, should end with "EventHandler".
    - `infrastructure`(folder:1): Contains the infrastructure code that is related to the aggregate root.
        - `XxxMongoRepository`(class:N): The repository implementations, should end with "MongoRepository" for MongoDB.
    - `job`(folder:1):Contains background jobs that are related to the aggregate root.
        - XxxScheduler(class:N): Scheduling configuration, should end with "Scheduler"
        - XxxJob(class:N): Represents a background job, should end with "Job". A job might run multiple tasks.
    - `query`(folder:1): For querying data, "query" represents the "Q"
      in [CQRS](https://microservices.io/patterns/data/cqrs.html).
        - `XxxQueryService`(class:N): The
          facade [application service](https://ddd-practitioners.com/home/glossary/application-service/) class for
          queries, should end with "QueryService".
        - `XxxQuery`(class:N): Request class of a query, should end with "Query".
        - `QXxx`(class:N): Response class of a query, should start with the letter "Q".
    - `XxxController`(class:1): The controller class, should end with "Controller".

