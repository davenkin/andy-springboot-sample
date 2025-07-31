package deviceet.user.infrastructure;

import deviceet.common.infrastructure.AbstractMongoRepository;
import deviceet.common.model.Entity;
import deviceet.user.domain.CachedOrgUser;
import deviceet.user.domain.CachedOrgUsers;
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
import static deviceet.common.utils.Constants.ORG_USERS_CACHE;
import static deviceet.common.utils.Constants.USER_COLLECTION;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CachedMongoUserRepository extends AbstractMongoRepository<User> {

    @Cacheable(value = ORG_USERS_CACHE, key = "#orgId")
    public CachedOrgUsers cachedOrgUsers(String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");

        Query query = query(where(Entity.Fields.orgId).is(orgId)).with(by(ASC, "createdAt"));
        query.fields().include("name");

        List<CachedOrgUser> cachedOrgUsers = mongoTemplate.find(query, CachedOrgUser.class, USER_COLLECTION);
        return CachedOrgUsers.builder().users(cachedOrgUsers).build();
    }

    @Caching(evict = {@CacheEvict(value = ORG_USERS_CACHE, key = "#orgId")})
    public void evictCachedOrgUsers(String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");

        log.debug("Evicted cached users for org[{}].", orgId);
    }
}
