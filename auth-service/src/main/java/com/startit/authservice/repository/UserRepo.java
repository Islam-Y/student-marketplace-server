package com.startit.authservice.repository;

import com.startit.authservice.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
}
