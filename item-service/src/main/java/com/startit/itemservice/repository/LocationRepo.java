package com.startit.itemservice.repository;

import com.startit.itemservice.entity.LocationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepo extends CrudRepository<LocationEntity, Long> {
    LocationEntity findByName(String name);
    Page<LocationEntity> findAll(Pageable pageable);
}
