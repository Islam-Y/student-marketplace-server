package com.startit.itemservice.service;

import com.startit.itemservice.mapper.StatusMapper;
import com.startit.itemservice.repository.StatusRepo;
import com.startit.itemservice.transfer.Status;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatusService {

    private final StatusRepo repo;

    private static final StatusMapper MAPPER = StatusMapper.INSTANCE;

    public Long save(Status status) {
        try {
            var entity = MAPPER.toEntity(status);
            return repo.save(entity).getId();
        } catch (Exception ex) {
            throw new RuntimeException("Невозможно сохранить объект!");
        }
    }

    public Page<Status> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(MAPPER::toDto);
    }
}
