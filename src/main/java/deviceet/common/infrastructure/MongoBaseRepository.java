package deviceet.common.infrastructure;

import deviceet.common.event.DomainEvent;
import deviceet.common.event.publish.PublishingDomainEventDao;
import deviceet.common.model.AggregateRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static deviceet.common.utils.CommonUtils.requireNonBlank;
import static deviceet.common.utils.CommonUtils.singleParameterizedArgumentClassOf;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

// Base class for all repositories

@SuppressWarnings({"unchecked"})
public abstract class MongoBaseRepository<AR extends AggregateRoot> {
    private final Map<String, Class<?>> repositoryArClassMap = new ConcurrentHashMap<>();

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    private PublishingDomainEventDao domainEventDao;

    @Transactional
    public void save(AR it) {
        requireNonNull(it, arName() + " must not be null.");
        requireNonBlank(it.getId(), arName() + " ID must not be blank.");

        if (!isEmpty(it.getEvents())) {
            saveEvents(it.getEvents());
        }

        mongoTemplate.save(it);
    }

    @Transactional
    public void delete(AR it) {
        requireNonNull(it, arName() + " must not be null.");

        if (!isEmpty(it.getEvents())) {
            saveEvents(it.getEvents());
        }
        mongoTemplate.remove(it);
    }

    public AR byId(String id) {
        requireNonBlank(id, arName() + " ID must not be blank.");
        return (AR) mongoTemplate.findById(id, arClass());
    }

    private Class<?> arClass() {
        String className = this.getClass().getName();

        if (!repositoryArClassMap.containsKey(className)) {
            Class<?> arClass = singleParameterizedArgumentClassOf(this.getClass());
            if (arClass != null) {
                repositoryArClassMap.put(className, arClass);
            }
        }

        return repositoryArClassMap.get(className);
    }

    private String arName() {
        return arClass().getSimpleName();
    }

    private void saveEvents(List<DomainEvent> events) {
        if (!isEmpty(events)) {
            List<DomainEvent> orderedEvents = events.stream().sorted(comparing(DomainEvent::getRaisedAt)).toList();
            domainEventDao.stage(orderedEvents);
        }
    }
}
