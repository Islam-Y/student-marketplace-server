package org.startit.objectservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.startit.objectservice.entity.PhotoEntity;

import java.util.Optional;

public interface PhotoRepo extends CrudRepository<PhotoEntity, Long> {
    Optional<PhotoEntity> findByItemId(String itemId);
}