package com.newbie.starter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbie.config.AppCacheConfig;
import com.newbie.manager.TwoLevelCacheManager;
import io.netty.util.internal.StringUtil;
import lombok.SneakyThrows;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableConfigurationProperties(AppCacheConfig.class)
public class SecondLevelCacheConfig {
    @Autowired
    public AppCacheConfig appCacheConfig;

    @Value("${spring.application.name}")
    public String applicationName;

    public static String appCacheTopic;
    @Bean
    public TwoLevelCacheManager cacheManager(StringRedisTemplate redisTemplate) {
        RedisCacheWriter writer = RedisCacheWriter.lockingRedisCacheWriter(redisTemplate.getConnectionFactory());
        Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        redisSerializer.setObjectMapper(objectMapper);
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));
        var cacheManager = new TwoLevelCacheManager(this, redisTemplate, writer, cacheConfiguration);
        return cacheManager;
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        var container = new RedisMessageListenerContainer();
        appCacheTopic =appCacheConfig.getTopic();
        container.setConnectionFactory(connectionFactory);
        if(!StringUtil.isNullOrEmpty(applicationName)) {
            appCacheTopic =applicationName + "-" + appCacheTopic;
        }
        container.addMessageListener(listenerAdapter, new PatternTopic(appCacheTopic));
        return container;
    }
    @Bean
    MessageListenerAdapter listenerAdapter(final TwoLevelCacheManager cacheManager) {
        return new MessageListenerAdapter(new MessageListener() {
            @SneakyThrows
            public void onMessage(Message message, byte[] pattern) {
                var cacheName = new String(message.getBody(), "UTF-8");
                cacheManager.receiver(cacheName);
            }
        });
    }
}
