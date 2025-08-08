package deviceet.sample.equipment.infrastructure;

import deviceet.business.device.domain.Device;
import deviceet.common.infrastructure.AbstractMongoRepository;
import deviceet.common.model.AggregateRoot;
import deviceet.sample.equipment.domain.Equipment;
import deviceet.sample.equipment.domain.EquipmentSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static deviceet.common.model.AggregateRoot.Fields.createdAt;
import static deviceet.common.utils.CommonUtils.requireNonBlank;
import static deviceet.sample.equipment.domain.Equipment.EQUIPMENT_COLLECTION;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CachedMongoEquipmentRepository extends AbstractMongoRepository<Device> {
    private static final String ORG_EQUIPMENT_CACHE = "ORG_EQUIPMENTS";

    @Cacheable(value = ORG_EQUIPMENT_CACHE, key = "#orgId")
    public List<EquipmentSummary> cachedEquipmentSummaries(String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");

        Query query = query(where(AggregateRoot.Fields.orgId).is(orgId)).with(by(ASC, createdAt));
        query.fields().include(AggregateRoot.Fields.orgId, Equipment.Fields.name, Equipment.Fields.status);
        return mongoTemplate.find(query, EquipmentSummary.class, EQUIPMENT_COLLECTION);
    }

    @Caching(evict = {@CacheEvict(value = ORG_EQUIPMENT_CACHE, key = "#orgId")})
    public void evictCachedEquipmentSummaries(String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");

        log.debug("Evicted cached equipment summaries for org[{}].", orgId);
    }
}
