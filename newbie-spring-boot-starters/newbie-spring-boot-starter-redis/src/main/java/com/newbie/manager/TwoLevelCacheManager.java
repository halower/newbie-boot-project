package com.newbie.manager;

import com.newbie.starter.SecondLevelCacheConfig;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;

public class TwoLevelCacheManager extends RedisCacheManager {
    private SecondLevelCacheConfig redisCacheConfig;
    RedisTemplate redisTemplate;

    public TwoLevelCacheManager(SecondLevelCacheConfig redisCacheConfig, RedisTemplate redisTemplate, RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.redisCacheConfig = redisCacheConfig;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Cache decorateCache(Cache cache) {
        return new RedisAndLocalCache(this, (RedisCache) cache);
    }
    public void publishMessage(String cacheName) {
        this.redisTemplate.convertAndSend(redisCacheConfig.appCacheTopic, cacheName);
    }

    public void receiver(String name) {
        RedisAndLocalCache cache = ((RedisAndLocalCache) this.getCache(name));
        if (cache != null) {
            cache.clearLocal();
        }
    }
}
