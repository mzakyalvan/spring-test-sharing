package com.tiket.poc.testing.redis;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

/**
 * Customize redis cache manager.
 *
 * @author zakyalvan
 */
@Component
public class RedisCacheCustomizer implements CacheManagerCustomizer<RedisCacheManager> {
    @Override
    public void customize(RedisCacheManager cacheManager) {
        cacheManager.setUsePrefix(true);
        cacheManager.setDefaultExpiration(60);
        cacheManager.setCachePrefix(new DefaultRedisCachePrefix("-"));
    }
}
