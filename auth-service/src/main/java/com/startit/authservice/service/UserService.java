package com.startit.authservice.service;

import com.startit.authservice.mapper.UserMapper;
import com.startit.authservice.repository.UserRepo;
import com.startit.authservice.transfer.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepo repo;

    private static final UserMapper MAPPER = UserMapper.INSTANCE;

    public Long save(User user) {
        var entity = MAPPER.toEntity(user);
        return repo.save(entity).getId();
    }

    public Optional<User> findById(Long id) {
        return repo.findById(id).map(MAPPER::toDto);
    }

    public Optional<User> findByUsername(String username) { return repo.findByUsername(username).map(MAPPER::toDto); }
}
