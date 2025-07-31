package deviceet.common.infrastructure;

import deviceet.common.event.DomainEvent;
import deviceet.common.event.publish.PublishingDomainEventDao;
import deviceet.common.exception.ServiceException;
import deviceet.common.model.Entity;
import deviceet.common.operator.CurrentOperatorProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static deviceet.common.exception.ErrorCode.ENTITY_NOT_FOUND;
import static deviceet.common.exception.ErrorCode.NOT_SAME_ORG;
import static deviceet.common.utils.CommonUtils.requireNonBlank;
import static deviceet.common.utils.CommonUtils.singleParameterizedArgumentClassOf;
import static deviceet.common.utils.Constants.MONGO_ID;
import static deviceet.common.utils.MapUtils.mapOf;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

// Base class for all repositories

@Slf4j
@SuppressWarnings({"unchecked"})
public abstract class AbstractMongoRepository<E extends Entity> {
    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    private PublishingDomainEventDao publishingDomainEventDao;

    @Autowired
    private CurrentOperatorProvider currentOperatorProvider;

    private final Class<?> entityClass;

    protected AbstractMongoRepository() {
        this.entityClass = singleParameterizedArgumentClassOf(this.getClass());
    }

    @Transactional
    public void save(E entity) {
        requireNonNull(entity, entityType() + " must not be null.");
        requireNonBlank(entity.getId(), entityType() + " ID must not be blank.");

        mongoTemplate.save(entity);
        stageEvents(entity.getEvents());
        entity.clearEvents();
    }

    @Transactional
    public void save(List<E> entities) {
        if (isEmpty(entities)) {
            return;
        }
        checkSameOrg(entities);
        List<DomainEvent> events = new ArrayList<>();
        entities.forEach(entity -> {
            if (isNotEmpty(entity.getEvents())) {
                events.addAll(entity.getEvents());
            }
            mongoTemplate.save(entity);
            entity.clearEvents();
        });

        stageEvents(events);
    }

    @Transactional
    public void delete(E entity) {
        requireNonNull(entity, entityType() + " must not be null.");
        requireNonBlank(entity.getId(), entityType() + " ID must not be blank.");

        mongoTemplate.remove(entity);
        stageEvents(entity.getEvents());
        entity.clearEvents();
    }

    @Transactional
    public void delete(List<E> entities) {
        if (isEmpty(entities)) {
            return;
        }
        checkSameOrg(entities);
        List<DomainEvent> events = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        entities.forEach(entity -> {
            if (isNotEmpty(entity.getEvents())) {
                events.addAll(entity.getEvents());
            }
            ids.add(entity.getId());
            entity.clearEvents();
        });

        mongoTemplate.remove(query(where(MONGO_ID).in(ids)), entityClass);
        stageEvents(events);
    }

    public E byId(String id) {
        requireNonBlank(id, entityType() + " ID must not be blank.");

        Object it = mongoTemplate.findById(id, entityClass);
        if (it == null) {
            throw new ServiceException(ENTITY_NOT_FOUND, "Entity not found.",
                    mapOf("type", entityType(), "id", id));
        }

        return (E) it;
    }

    public Optional<E> byIdOptional(String id) {
        requireNonBlank(id, entityType() + " ID must not be blank.");

        Object it = mongoTemplate.findById(id, entityClass);
        return it == null ? empty() : Optional.of((E) it);
    }

    public E byId(String id, String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");
        requireNonBlank(id, entityType() + " ID must not be blank.");
        Query query = query(where(Entity.Fields.orgId).is(orgId).and(MONGO_ID).is(id));

        Object entity = mongoTemplate.findOne(query, entityClass);
        if (entity == null) {
            throw new ServiceException(ENTITY_NOT_FOUND, "Entity not found.",
                    mapOf("type", entityType(), "id", id, "orgId", orgId));
        }
        return (E) entity;
    }

    public Optional<E> byIdOptional(String id, String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");
        requireNonBlank(id, entityType() + " ID must not be blank.");
        Query query = query(where(Entity.Fields.orgId).is(orgId).and(MONGO_ID).is(id));

        Object entity = mongoTemplate.findOne(query, entityClass);
        return entity == null ? empty() : Optional.of((E) entity);
    }

    public boolean exists(String id, String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");
        requireNonBlank(id, entityType() + " ID must not be blank.");

        Query query = query(where(Entity.Fields.orgId).is(orgId).and(MONGO_ID).is(id));
        return mongoTemplate.exists(query, entityClass);
    }

    private String entityType() {
        return this.entityClass.getSimpleName();
    }

    private void stageEvents(List<DomainEvent> events) {
        if (isNotEmpty(events)) {
            List<DomainEvent> orderedEvents = events.stream().sorted(comparing(DomainEvent::getRaisedAt)).toList();
            String raisedBy = currentOperatorProvider.currentOperator();
            orderedEvents.forEach(event -> event.raisedBy(raisedBy));
            publishingDomainEventDao.stage(orderedEvents);
        }
    }

    private void checkSameOrg(Collection<E> entities) {
        Set<String> orgIdS = entities.stream().map(E::getOrgId).collect(toImmutableSet());
        if (orgIdS.size() > 1) {
            Set<String> allEntityIds = entities.stream().map(Entity::getId).collect(toImmutableSet());
            throw new ServiceException(NOT_SAME_ORG, "All entities should belong to the same organization.", "entityIds", allEntityIds);
        }
    }
}
