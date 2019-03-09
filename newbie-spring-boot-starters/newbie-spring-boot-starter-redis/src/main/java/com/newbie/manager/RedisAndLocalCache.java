package com.newbie.manager;

import lombok.extern.java.Log;
import lombok.var;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

@Log
class RedisAndLocalCache implements Cache {
    ConcurrentHashMap<Object, Object> local = new ConcurrentHashMap<Object, Object>();
    RedisCache redisCache;
    TwoLevelCacheManager cacheManager;

    public RedisAndLocalCache(TwoLevelCacheManager cacheManager, RedisCache redisCache) {
        this.redisCache = redisCache;
        this.cacheManager = cacheManager;
    }

    @Override
    public String getName() {
        return redisCache.getName();
    }

    @Override
    public Object getNativeCache() {
        return redisCache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        var wrapper = (ValueWrapper) local.get(key);
        if (wrapper != null) {
            return wrapper;
        } else {
            wrapper = redisCache.get(key);
            if (wrapper != null) {
                local.put(key, wrapper);
            }
            return wrapper;
        }

    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return redisCache.get(key, type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return redisCache.get(key, valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
        redisCache.put(key, value);
        clearOtherJVM();
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        var v = redisCache.putIfAbsent(key, value);
        clearOtherJVM();
        return v;
    }

    @Override
    public void evict(Object key) {
        redisCache.evict(key);
        clearOtherJVM();
    }

    @Override
    public void clear() {
        redisCache.clear();
    }

    public void clearLocal() {
        this.local.clear();
    }

    protected void clearOtherJVM() {
        cacheManager.publishMessage(redisCache.getName());
    }
}
