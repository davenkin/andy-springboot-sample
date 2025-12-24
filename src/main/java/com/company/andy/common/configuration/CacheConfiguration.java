package com.company.andy.common.configuration;

import com.company.andy.common.util.Constants;
import com.company.andy.feature.equipment.infrastructure.CachedOrgEquipmentSummaries;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import static com.company.andy.common.util.Constants.ORG_EQUIPMENTS_CACHE;
import static java.time.Duration.ofDays;
import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@EnableCaching
@Configuration(proxyBeanMethods = false)
public class CacheConfiguration {

    @Bean
    public RedisCacheManagerBuilderCustomizer redisBuilderCustomizer(ObjectMapper objectMapper) {
        return builder -> builder
                .cacheDefaults(defaultCacheConfig()
                        .prefixCacheNameWith(Constants.CACHE_PREFIX)
                        .serializeValuesWith(fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
                        .entryTtl(ofDays(7)))
                .withCacheConfiguration(ORG_EQUIPMENTS_CACHE, defaultCacheConfig()
                        .prefixCacheNameWith(Constants.CACHE_PREFIX)
                        .serializeValuesWith(fromSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, CachedOrgEquipmentSummaries.class)))
                        .entryTtl(ofDays(7)))
                ;
    }

}
