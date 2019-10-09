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
        this.redisTemplate.convertAndSend(SecondLevelCacheConfig.appCacheTopic, cacheName);
    }

    public void receiver(String name) {
        RedisAndLocalCache cache = ((RedisAndLocalCache) this.getCache(name));
        if (cache != null) {
            cache.clearLocal();
        }
    }
}
