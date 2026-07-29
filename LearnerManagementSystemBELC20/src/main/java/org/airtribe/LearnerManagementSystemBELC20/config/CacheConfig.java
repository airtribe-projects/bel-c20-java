package org.airtribe.LearnerManagementSystemBELC20.config;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;


@Configuration
public class CacheConfig {

  @Bean
  public RedisCacheConfiguration cacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(60))
        .disableCachingNullValues().serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
            GenericJacksonJsonRedisSerializer.builder().enableUnsafeDefaultTyping().build()));
  }
}
