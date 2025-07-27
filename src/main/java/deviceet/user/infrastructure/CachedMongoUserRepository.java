package deviceet.user.infrastructure;

import deviceet.common.infrastructure.AbstractMongoRepository;
import deviceet.common.model.AggregateRoot;
import deviceet.user.domain.CachedTenantUser;
import deviceet.user.domain.CachedTenantUsers;
import deviceet.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static deviceet.common.utils.CommonUtils.requireNonBlank;
import static deviceet.common.utils.Constants.TENANT_USERS_CACHE;
import static deviceet.common.utils.Constants.USER_COLLECTION;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CachedMongoUserRepository extends AbstractMongoRepository<User> {

    @Cacheable(value = TENANT_USERS_CACHE, key = "#tenantId")
    public CachedTenantUsers cachedTenantUsers(String tenantId) {
        requireNonBlank(tenantId, "Tenant ID must not be blank.");

        Query query = query(where(AggregateRoot.Fields.tenantId).is(tenantId)).with(by(ASC, "createdAt"));
        query.fields().include("name");

        List<CachedTenantUser> tenantCachedApps = mongoTemplate.find(query, CachedTenantUser.class, USER_COLLECTION);
        return CachedTenantUsers.builder().users(tenantCachedApps).build();
    }

    @Caching(evict = {@CacheEvict(value = TENANT_USERS_CACHE, key = "#tenantId")})
    public void evictCachedTenantUsers(String tenantId) {
        requireNonBlank(tenantId, "Tenant ID must not be blank.");

        log.debug("Evicted cached users for tenant[{}].", tenantId);
    }
}
