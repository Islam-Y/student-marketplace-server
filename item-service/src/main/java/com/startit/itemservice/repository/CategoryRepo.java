package com.startit.itemservice.repository;

import com.startit.itemservice.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepo extends CrudRepository<CategoryEntity, Long>, JpaSpecificationExecutor<CategoryEntity> {
    CategoryEntity findByName(String name);
    Page<CategoryEntity> findAll(Pageable pageable);
}
