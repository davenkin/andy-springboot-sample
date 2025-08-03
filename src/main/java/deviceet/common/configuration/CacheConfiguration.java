package deviceet.common.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import static deviceet.common.utils.Constants.ORG_DEVICES_CACHE;
import static java.time.Duration.ofDays;
import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@EnableCaching
@Configuration(proxyBeanMethods = false)
public class CacheConfiguration {
    private static final String CACHE_PREFIX = "Cache:";

    @Bean
    public RedisCacheManagerBuilderCustomizer redisBuilderCustomizer(ObjectMapper objectMapper) {
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        return builder -> builder
                .withCacheConfiguration(ORG_DEVICES_CACHE, defaultCacheConfig()
                        .prefixCacheNameWith(CACHE_PREFIX)
                        .serializeValuesWith(fromSerializer(serializer))
                        .entryTtl(ofDays(7)));
    }

}
