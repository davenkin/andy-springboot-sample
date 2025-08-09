# Use DDD architectural style

## Context

When developing enterprise applications, there are various architectural styles that can be adopted. Some of the common styles include:

- CRUD style data manipulating: Use some pure technical frameworks to manipulate data, there is not much business modeling in it but just
  CRUD(Create, Read, Update, Delete) operations.
- Domain-Driven Design (DDD): Focus on the business domains, and let code to express the domain rules and behaviors, aims to decouple the
  domain complexity from technical complexity.

## Decision

We choose **DDD** as our architectural style as it helps us to focus on the core domain and its complexities, allowing us to create a more
maintainable and scalable architecture. Programming is not just about CRUD or calling APIs. It's about solving problems and creating value,
DDD excels on this.

## Implementation

This is an ADR but not a lecture on DDD, so here we only list some recommended resources online:

- DDD books: https://www.workingsoftware.dev/the-ultimate-list-of-domain-driven-design-books-in-2024/
- DDD learning series: https://docs.mryqr.com/ddd-introduction/
- DDD code practices: https://www.cnblogs.com/davenkin/p/ddd-coding-practices.html
