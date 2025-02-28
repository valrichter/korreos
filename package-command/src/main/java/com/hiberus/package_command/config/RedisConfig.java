package com.hiberus.package_command.config;

import com.hiberus.model.PackageModel;
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

    @Value("${spring.data.redis.connections.riders.host}")
    private String ridersHost;

    @Value("${spring.data.redis.connections.riders.port}")
    private int ridersPort;

    @Value("${spring.data.redis.connections.riders.database}")
    private int ridersDatabase;

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

    @Bean(name = "ridersRedisConnectionFactory")
    public RedisConnectionFactory ridersRedisConnectionFactory() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(ridersHost, ridersPort);
        connectionFactory.setDatabase(ridersDatabase);
        return connectionFactory;
    }

    @Bean(name = "ridersRedisTemplate")
    public RedisTemplate<String, String> ridersRedisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(this.ridersRedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}

