package deviceet.common.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import deviceet.device.domain.cache.CachedOrgDevices;
import deviceet.user.domain.CachedOrgUsers;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import static deviceet.common.utils.Constants.ORG_DEVICES_CACHE;
import static deviceet.common.utils.Constants.ORG_USERS_CACHE;
import static java.time.Duration.ofDays;
import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@EnableCaching
@Configuration(proxyBeanMethods = false)
public class CacheConfiguration {
    private static final String CACHE_PREFIX = "Cache:";

    @Bean
    public RedisCacheManagerBuilderCustomizer redisBuilderCustomizer(ObjectMapper objectMapper) {
        var orgUsersSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, CachedOrgUsers.class);
        var orgDevicesSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, CachedOrgDevices.class);

        return builder -> builder
                .withCacheConfiguration(ORG_USERS_CACHE, defaultCacheConfig()
                        .prefixCacheNameWith(CACHE_PREFIX)
                        .serializeValuesWith(fromSerializer(orgUsersSerializer))
                        .entryTtl(ofDays(7)))
                .withCacheConfiguration(ORG_DEVICES_CACHE, defaultCacheConfig()
                        .prefixCacheNameWith(CACHE_PREFIX)
                        .serializeValuesWith(fromSerializer(orgDevicesSerializer))
                        .entryTtl(ofDays(7)));
    }

}
