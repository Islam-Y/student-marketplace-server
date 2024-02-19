package com.startit.itemservice.repository;


import com.startit.itemservice.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepo extends CrudRepository<ItemEntity, Long>, JpaSpecificationExecutor<ItemEntity> {
    Page<ItemEntity> findAll(Pageable pageable);
    Page<ItemEntity> findBySellerId(Long sellerId, Pageable pageable);
}
