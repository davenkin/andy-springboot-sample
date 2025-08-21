package com.company.andy.common.infrastructure;

import com.company.andy.common.event.DomainEvent;
import com.company.andy.common.event.publish.PublishingDomainEventDao;
import com.company.andy.common.exception.ServiceException;
import com.company.andy.common.model.AggregateRoot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.company.andy.common.exception.ErrorCode.AR_NOT_FOUND;
import static com.company.andy.common.exception.ErrorCode.NOT_SAME_ORG;
import static com.company.andy.common.util.CommonUtils.requireNonBlank;
import static com.company.andy.common.util.CommonUtils.singleParameterizedArgumentClassOf;
import static com.company.andy.common.util.Constants.MONGO_ID;
import static com.company.andy.common.util.NullableMapUtils.mapOf;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

// Base class for all repositories
// Repository is per AggregateRoot type, namely only AggregateRoot can have Repository

@SuppressWarnings("unchecked")
@Slf4j
public abstract class AbstractMongoRepository<AR extends AggregateRoot> {
    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    private PublishingDomainEventDao publishingDomainEventDao;

    private final Class<?> arClass;

    protected AbstractMongoRepository() {
        this.arClass = singleParameterizedArgumentClassOf(this.getClass());
    }

    @Transactional
    public void save(AR ar) {
        requireNonNull(ar, arType() + " must not be null.");
        requireNonBlank(ar.getId(), arType() + " ID must not be blank.");

        ar.onModify(this.currentOperatorId());
        mongoTemplate.save(ar);
        stageEvents(ar.getEvents());
        ar.clearEvents();
    }

    @Transactional
    public void save(List<AR> ars) {
        if (isEmpty(ars)) {
            return;
        }
        checkSameOrg(ars);
        List<DomainEvent> events = new ArrayList<>();
        ars.forEach(ar -> {
            if (isNotEmpty(ar.getEvents())) {
                events.addAll(ar.getEvents());
            }
            ar.onModify(this.currentOperatorId());
            mongoTemplate.save(ar);
            ar.clearEvents();
        });

        stageEvents(events);
    }

    @Transactional
    public void delete(AR ar) {
        requireNonNull(ar, arType() + " must not be null.");
        requireNonBlank(ar.getId(), arType() + " ID must not be blank.");

        ar.onDelete();
        mongoTemplate.remove(ar);
        stageEvents(ar.getEvents());
        ar.clearEvents();
    }

    @Transactional
    public void delete(List<AR> ars) {
        if (isEmpty(ars)) {
            return;
        }
        checkSameOrg(ars);
        List<DomainEvent> events = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        ars.forEach(ar -> {
            ar.onDelete();
            if (isNotEmpty(ar.getEvents())) {
                events.addAll(ar.getEvents());
            }
            ids.add(ar.getId());
            ar.clearEvents();
        });

        mongoTemplate.remove(query(where(MONGO_ID).in(ids)), arClass);
        stageEvents(events);
    }

    public AR byId(String id) {
        requireNonBlank(id, arType() + " ID must not be blank.");

        Object ar = mongoTemplate.findById(id, arClass);
        if (ar == null) {
            throw new ServiceException(AR_NOT_FOUND, arType() + " not found.",
                    mapOf("type", arType(), "id", id));
        }

        return (AR) ar;
    }

    public Optional<AR> byIdOptional(String id) {
        requireNonBlank(id, arType() + " ID must not be blank.");

        Object ar = mongoTemplate.findById(id, arClass);
        return ar == null ? empty() : Optional.of((AR) ar);
    }

    public AR byId(String id, String orgId) {
        requireNonBlank(id, arType() + " ID must not be blank.");
        requireNonBlank(orgId, "orgId must not be blank.");

        AR ar = this.byId(id);
        if (Objects.equals(ar.getOrgId(), orgId)) {
            return ar;
        }
        throw new ServiceException(AR_NOT_FOUND, arType() + " not found.",
                mapOf("type", arType(), "id", id, "orgId", orgId));
    }

    public Optional<AR> byIdOptional(String id, String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");
        requireNonBlank(id, arType() + " ID must not be blank.");

        Optional<AR> ar = byIdOptional(id);
        return ar.isPresent() && Objects.equals(ar.get().getOrgId(), orgId) ? ar : empty();
    }

    public boolean exists(String id) {
        requireNonBlank(id, arType() + " ID must not be blank.");

        Query query = query(where(MONGO_ID).is(id));
        return mongoTemplate.exists(query, arClass);
    }

    public boolean exists(String id, String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");
        requireNonBlank(id, arType() + " ID must not be blank.");

        Query query = query(where(AggregateRoot.Fields.orgId).is(orgId).and(MONGO_ID).is(id));
        return mongoTemplate.exists(query, arClass);
    }

    private String arType() {
        return this.arClass.getSimpleName();
    }

    private void stageEvents(List<DomainEvent> events) {
        if (isNotEmpty(events)) {
            List<DomainEvent> orderedEvents = events.stream().sorted(comparing(DomainEvent::getRaisedAt)).toList();
            orderedEvents.forEach(event -> event.raisedBy(this.currentOperatorId()));
            publishingDomainEventDao.stage(orderedEvents);
        }
    }

    private void checkSameOrg(Collection<AR> ars) {
        Set<String> orgIdS = ars.stream().map(AR::getOrgId).collect(toImmutableSet());
        if (orgIdS.size() > 1) {
            Set<String> allArIds = ars.stream().map(AggregateRoot::getId).collect(toImmutableSet());
            throw new ServiceException(NOT_SAME_ORG, "All ARs should belong to the same organization.", "arIds", allArIds);
        }
    }

    // Here currentOperatorId() is only used for audit purpose, and should not be used as business data
    private String currentOperatorId() {
        return null;// todo: impl, get current operator id from e.g. SpringSecurityContext
    }
}
