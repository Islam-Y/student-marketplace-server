package com.startit.itemservice.repository;

import com.startit.itemservice.entity.StatusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface StatusRepo extends CrudRepository<StatusEntity, Long> {
    StatusEntity findByName(String name);
    Page<StatusEntity> findAll(Pageable pageable);
}
