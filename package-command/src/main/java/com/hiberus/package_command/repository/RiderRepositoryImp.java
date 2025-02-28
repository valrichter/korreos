package com.hiberus.package_command.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RiderRepositoryImp implements RiderRepository {

    @Autowired
    @Qualifier("ridersRedisTemplate")
    private RedisTemplate<String, String> ridersRedisTemplate;

    @Override
    public String getRiderById(String id) {
        return ridersRedisTemplate.opsForValue().get(id);
    }
}
