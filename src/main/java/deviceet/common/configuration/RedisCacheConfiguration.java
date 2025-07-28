package deviceet.common.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import deviceet.common.configuration.profile.DisableForCI;
import deviceet.user.domain.CachedTenantUsers;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import static deviceet.common.utils.Constants.TENANT_USERS_CACHE;
import static java.time.Duration.ofDays;
import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@DisableForCI
@Configuration(proxyBeanMethods = false)
public class RedisCacheConfiguration {
    private static final String CACHE_PREFIX = "Cache:";

    @Bean
    public RedisCacheManagerBuilderCustomizer redisBuilderCustomizer(ObjectMapper objectMapper) {
        var tenantUsersSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, CachedTenantUsers.class);

        return builder -> builder
                .withCacheConfiguration(TENANT_USERS_CACHE, defaultCacheConfig()
                        .prefixCacheNameWith(CACHE_PREFIX)
                        .serializeValuesWith(fromSerializer(tenantUsersSerializer))
                        .entryTtl(ofDays(7)));
    }
}
