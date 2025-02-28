package com.hiberus.package_query.config;

import com.hiberus.model.PackageModel;
import com.hiberus.package_query.dto.PackageHistoryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.connections.packages.host}")
    private String packagesHost;

    @Value("${spring.data.redis.connections.packages.port}")
    private int packagesPort;

    @Value("${spring.data.redis.connections.packages.database}")
    private int packagesDatabase;

    @Value("${spring.data.redis.connections.last-topic.host}")
    private String lastTopicHost;

    @Value("${spring.data.redis.connections.last-topic.port}")
    private int lastTopicPort;

    @Value("${spring.data.redis.connections.last-topic.database}")
    private int lastTopicDatabase;

    @Bean(name = "packagesRedisConnectionFactory")
    public RedisConnectionFactory packagesRedisConnectionFactory() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(packagesHost, packagesPort);
        connectionFactory.setDatabase(packagesDatabase);
        return connectionFactory;
    }

    @Bean(name = "packagesRedisTemplate")
    public RedisTemplate<String, PackageModel> packagesRedisTemplate() {
        RedisTemplate<String, PackageModel> template = new RedisTemplate<>();
        template.setConnectionFactory(this.packagesRedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean(name = "packageHistoryRedisConnectionFactory")
    public RedisConnectionFactory packageHistoryRedisConnectionFactory() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(lastTopicHost, lastTopicPort);
        connectionFactory.setDatabase(lastTopicDatabase);
        return connectionFactory;
    }

    @Bean(name = "packageHistoryRedisTemplate")
    public RedisTemplate<String, PackageHistoryDTO> lastTopicRedisTemplate() {
        RedisTemplate<String, PackageHistoryDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(this.packageHistoryRedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}

