# Domain event consuming

todo: impl

## Context

## Decision

## Implementation

- Do not change the name and location of event handler classes after they are created, otherwise the event consuming mechanism
  might not work as expected because the event idempotency is based on event handlers' full class names, so choose carefully at the beginning.

- One event can be consumed by multiple event handlers independently.