# Use CQRS

## Context

Software usually provides two types of operations: the **write** side and the **read** side. Write operations represent
some
commands you send to you software to do something, and the read side merely queries some data without changing the
software state, hence write is also called **command** and read is also called **query**.

When it comes to implementation, command and query can be modeled with the same set of objects. Or they can be treated
separately using different models, which is
called [Command Query Responsibility Segregation (CQRS)](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/apply-simplified-microservice-cqrs-ddd-patterns).

CQRS can be full scale or lightweight.

In full scale CQRS, the command side and query side uses different database
tables to hold data, a message queueing system is usually used synchronize state from command side to query side.

In lightweight CQRS, the command side and query side uses the same database table, which is simpler than full scale
CQRS.

## Decision

We choose to use **lightweight CQRS**. Because we believe that the command side and query side are essentially very
different. In command side, business rules should be strictly validated and the business logic should be modeled
according to commonly accepted principles like [SOLID](https://en.wikipedia.org/wiki/SOLID)
and [GRASP](https://en.wikipedia.org/wiki/GRASP_(object-oriented_design)). In query side, the restrictions are much more
loosen and we can use whatever means to hit the database for fast and high performant queries.

Also we think CQRS should not be a heavyweight undertaking that scares developers away, but should be just enough to
fulfil its job.

Based on the above, the **lightweight CQRS** approach meets our needs and is our way to go.

## Implementation

There are 2 main points regarding lightweight CQRS implementation.

- The first one is that apart from CommandServices, we create standalone QueryServices to implement the query side. In
  QueryService, we use `MongoTemplate` to query database directly, bypassing the domain models, and also we use separate
  set of query objects other than domain objects. For example, in `EquipmentQueryService.listEquipments()`, we don't use
  the domain object of `Equipment`, instead a query model `QListedEquipment` is created and `MongoTemplate` is used to
  hit the database directly.

```java
    public Page<QListedEquipment> listEquipments(ListEquipmentQuery listEquipmentQuery, Pageable pageable, Principal principal) {
        Criteria criteria = where(AggregateRoot.Fields.orgId).is(principal.getOrgId());

        // more code omitted
        
        Query query = Query.query(criteria);

        // use query model QListedEquipment instead of domain model Equipment
        List<QListedEquipment> devices = mongoTemplate.find(query.with(pageable), QListedEquipment.class, EQUIPMENT_COLLECTION);
        return new PageImpl<>(devices, pageable, count);
    }
```

- The second point is that the database table serves as a container for both command side data and query side data. For
  example, in `Equipment`, besides its own coherent business fields, the `maintenanceRecordCount` is added for the query
  side.

```java
public class Equipment extends AggregateRoot {
    private String name; 
    private EquipmentStatus status;
    private String holder;
    private long maintenanceRecordCount; // Query side field
```

