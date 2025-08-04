package deviceet.business.device.infrastructure;

import deviceet.business.device.domain.Device;
import deviceet.business.device.domain.cache.CachedDeviceReference;
import deviceet.common.infrastructure.AbstractMongoRepository;
import deviceet.common.model.AggregateRoot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static deviceet.business.device.domain.Device.Fields.*;
import static deviceet.common.model.AggregateRoot.Fields.createdAt;
import static deviceet.common.utils.CommonUtils.requireNonBlank;
import static deviceet.common.utils.Constants.DEVICE_COLLECTION;
import static deviceet.common.utils.Constants.ORG_DEVICES_CACHE;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CachedMongoDeviceRepository extends AbstractMongoRepository<Device> {

    @Cacheable(value = ORG_DEVICES_CACHE, key = "#orgId")
    public List<CachedDeviceReference> cachedDeviceReferences(String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");

        Query query = query(where(AggregateRoot.Fields.orgId).is(orgId)).with(by(ASC, createdAt));
        query.fields().include(AggregateRoot.Fields.orgId, configuredName, cpuArchitecture, osType);
        return mongoTemplate.find(query, CachedDeviceReference.class, DEVICE_COLLECTION);
    }

    @Caching(evict = {@CacheEvict(value = ORG_DEVICES_CACHE, key = "#orgId")})
    public void evictCachedDeviceReferences(String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");

        log.debug("Evicted cached devices for org[{}].", orgId);
    }
}
