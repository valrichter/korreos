package com.hiberus.package_query.repository;

import com.hiberus.model.PackageModel;
import com.hiberus.package_query.dto.PackageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
public class PackageRepositoryImp implements PackageRepository {

    @Autowired
    @Qualifier("packagesRedisTemplate")
    private RedisTemplate<String, PackageModel> packagesRedisTemplate;

    @Override
    public PackageModel getPackageById(String id) {
        log.info("REPOSITORY - get package with id : '{}'", id);
        return packagesRedisTemplate.opsForValue().get(id);
    }

    @Override
    public List<PackageModel> getAllPackages() {
        log.info("REPOSITORY - get all packages");
        Set<String> keys = packagesRedisTemplate.keys("*");
        List<PackageModel> packages = new ArrayList<>();
        for (String key : keys) {
            packages.add(packagesRedisTemplate.opsForValue().get(key));
        }
        return packages;
    }
}
