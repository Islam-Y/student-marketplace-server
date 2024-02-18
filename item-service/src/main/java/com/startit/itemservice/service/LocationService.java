package com.startit.itemservice.service;

import com.startit.itemservice.mapper.LocationMapper;
import com.startit.itemservice.repository.LocationRepo;
import com.startit.itemservice.transfer.Location;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LocationService {

    private final LocationRepo repo;

    private static final LocationMapper MAPPER = LocationMapper.INSTANCE;

    public Long save(Location location) {
        try {
            var entity = MAPPER.toEntity(location);
            return repo.save(entity).getId();
        } catch (ConstraintViolationException ex) {
            throw new RuntimeException("Нарушен инвариант в поле " + ex.getConstraintName());
        } catch (Exception ex) {
            throw new RuntimeException("Невозможно сохранить объект!");
        }
    }

    public Page<Location> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(MAPPER::toDto);
    }
}
