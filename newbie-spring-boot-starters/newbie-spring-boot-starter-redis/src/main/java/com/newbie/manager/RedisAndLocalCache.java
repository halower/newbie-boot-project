/*
 * Apache License
 *
 * Copyright (c) 2019  halower (halower@foxmail.com).
 *
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

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
