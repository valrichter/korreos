package com.hiberus.package_command.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisRidersInfo {

    @Autowired
    @Qualifier("ridersRedisTemplate")
    private RedisTemplate<String, String> ridersRedisTemplate;

    @Bean
    public CommandLineRunner loadInitialData() {
        return args -> {
            ridersRedisTemplate.opsForValue().set("1", "Raul");
            ridersRedisTemplate.opsForValue().set("2", "Colapinto");
            ridersRedisTemplate.opsForValue().set("3", "Carlota");
            ridersRedisTemplate.opsForValue().set("4", "Facundo");
            ridersRedisTemplate.opsForValue().set("5", "Mauro");
            System.out.println("Initial data for 'riders' loaded into Redis");
        };
    }
}