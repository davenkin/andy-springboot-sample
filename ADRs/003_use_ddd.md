# Use DDD

## Context

When developing enterprise applications, there are various architectural styles that can be adopted. Some of the common
styles include:

- CRUD style data manipulating: Use some pure technical frameworks to manipulate data, there is not much business
  modeling in it but just
  CRUD(Create, Read, Update, Delete) operations.
- [Domain Driven Design (DDD)](https://martinfowler.com/bliki/DomainDrivenDesign.html): Focus on the business domains, and let code to express the domain rules and behaviors,
  aims to decouple the
  domain complexity from technical complexity.

## Decision

We choose **DDD** as our architectural style as it helps us to focus on the business problems first instead of technical
problems, allowing us to write more
maintainable and scalable code. Programming is not just about CRUD or calling APIs, it's about solving problems and
creating business value, and DDD excels on this.

## Implementation

This is an ADR but not a lecture on DDD, so here we only list some common DDD principles:

- Make sure everybody speaks the same language over the business domain, this includes domain experts, product owners,
  UX, DEVs and QAs. Also the same language should be used in the code.
- Remember, the sole reason that your software exists is that it solves a specific business problem. This business
  problem is the first letter `D` in `DDD`.
- Make a clear separation between domain code and technical code, this is why we have the concept of  `Domain Model`.
- In domain model, the most important concept is **AggregateRoot**s. You may roughly think of them as the major business
  entity
  classes in you code. Aggregate roots are the major places where your business logic happens.
- Sometimes, the business logic is not suitable for residing in Aggregate Root, so we create **DomainService**s to hold such
  business logic. But please pay attention that DomainServices are the last place you should put your business logic in,
  most of the time you should put business logic code inside your AggregateRoots.
- AggregateRoots represent the business logic, but not the use case, in order to bridge the use case and the business
  logic, DDD introduced **ApplicationService**. ApplicationService orchestrates the process flow from use case entrypoint to
  AggregateRoots. ApplicationService should not contain business logic.
- In DDD, we have both ApplicationService and DomainService. When you are creating a service class, you should know
  which kind of services you are creating.
- When retrieving and persisting AggregateRoots, we use **Repository**. Compared with Data Access Object(DAO), Repositories
  have a restriction that only AggregateRoot can have its Repository, other classes in the domain model should not have
  Repository. Also, Repository handles the whole AggregateRoot, but not its partial data.

For more detailed information on DDD you may refer to the following online resources:

- DDD books: https://www.workingsoftware.dev/the-ultimate-list-of-domain-driven-design-books-in-2024/
- DDD learning series: https://docs.mryqr.com/ddd-introduction/
- DDD code practices: https://www.cnblogs.com/davenkin/p/ddd-coding-practices.html
