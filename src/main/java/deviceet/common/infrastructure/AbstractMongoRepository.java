package deviceet.common.infrastructure;

import deviceet.common.event.DomainEvent;
import deviceet.common.event.publish.PublishingDomainEventDao;
import deviceet.common.exception.ServiceException;
import deviceet.common.model.AggregateRoot;
import deviceet.common.operator.CurrentPrincipalProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static deviceet.common.exception.ErrorCode.AR_NOT_FOUND;
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

@SuppressWarnings("unchecked")
@Slf4j
public abstract class AbstractMongoRepository<AR extends AggregateRoot> {
    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    private PublishingDomainEventDao publishingDomainEventDao;

    @Autowired
    private CurrentPrincipalProvider currentPrincipalProvider;

    private final Class<?> arClass;

    protected AbstractMongoRepository() {
        this.arClass = singleParameterizedArgumentClassOf(this.getClass());
    }

    @Transactional
    public void save(AR ar) {
        requireNonNull(ar, arType() + " must not be null.");
        requireNonBlank(ar.getId(), arType() + " ID must not be blank.");

        ar.onModify(currentPrincipalProvider.currentPrincipalUserId());
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
            ar.onModify(currentPrincipalProvider.currentPrincipalUserId());
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
        requireNonBlank(orgId, "orgId must not be blank.");
        requireNonBlank(id, arType() + " ID must not be blank.");
        Query query = query(where(AggregateRoot.Fields.orgId).is(orgId).and(MONGO_ID).is(id));

        Object ar = mongoTemplate.findOne(query, arClass);
        if (ar == null) {
            throw new ServiceException(AR_NOT_FOUND, arType() + " not found.",
                    mapOf("type", arType(), "id", id, "orgId", orgId));
        }
        return (AR) ar;
    }

    public Optional<AR> byIdOptional(String id, String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");
        requireNonBlank(id, arType() + " ID must not be blank.");
        Query query = query(where(AggregateRoot.Fields.orgId).is(orgId).and(MONGO_ID).is(id));

        Object ar = mongoTemplate.findOne(query, arClass);
        return ar == null ? empty() : Optional.of((AR) ar);
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
            String raisedBy = currentPrincipalProvider.currentPrincipalUserId();
            orderedEvents.forEach(event -> event.raisedBy(raisedBy));
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
}
