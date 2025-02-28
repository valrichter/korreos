package com.hiberus.package_command.repository;

import com.hiberus.model.PackageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class PackageRepositoryImp implements PackageRepository {

    @Autowired
    @Qualifier("packagesRedisTemplate")
    private RedisTemplate<String, PackageModel> packagesRedisTemplate;

    @Autowired
    @Qualifier("ridersRedisTemplate")
    private RedisTemplate<String, String> ridersRedisTemplate;

    @Override
    public PackageModel createPackage(PackageModel packageModel) {
        log.info("REPOSITORY - creating new package with id : '{}'", packageModel.getId());
        packagesRedisTemplate.opsForValue().set(packageModel.getId(), packageModel);
        return packagesRedisTemplate.opsForValue().get(packageModel.getId());
    }

    @Override
    public PackageModel updatePackage(PackageModel upadtedPackageDTO) {
        log.info("REPOSITORY - updating package with id : '{}'", upadtedPackageDTO.getId());
        PackageModel packageModel = packagesRedisTemplate.opsForValue().get(upadtedPackageDTO.getId());
        if (packageModel == null) {
            return null;
        }
        packagesRedisTemplate.opsForValue().set(upadtedPackageDTO.getId(), upadtedPackageDTO);
        return packagesRedisTemplate.opsForValue().get(upadtedPackageDTO.getId());
    }

    @Override
    public PackageModel deletePackageById(String id) {
        log.info("REPOSITORY - deleting package with id : '{}'", id);
        packagesRedisTemplate.delete(id);
        return packagesRedisTemplate.opsForValue().get(id);
    }

    @Override
    public PackageModel getPackageById(String id) {
        log.info("REPOSITORY - getting package with id : '{}'", id);
        return packagesRedisTemplate.opsForValue().get(id);
    }
}
