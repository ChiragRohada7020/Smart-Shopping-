package com.cdac.config;

import com.cdac.entity.ProductListWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, ProductListWrapper> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, ProductListWrapper> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key and HashKey as String
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value and HashValue as JSON
        Jackson2JsonRedisSerializer<ProductListWrapper> jsonSerializer =
                new Jackson2JsonRedisSerializer<>(ProductListWrapper.class);
        jsonSerializer.setObjectMapper(new ObjectMapper());

        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }
}