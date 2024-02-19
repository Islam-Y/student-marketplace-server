package com.startit.userservice.repository;

import com.startit.userservice.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
}
