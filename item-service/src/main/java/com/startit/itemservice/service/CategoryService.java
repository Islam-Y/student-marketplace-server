package com.startit.itemservice.service;

import com.startit.itemservice.mapper.CategoryMapper;
import com.startit.itemservice.repository.CategoryRepo;
import com.startit.itemservice.transfer.Category;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepo repo;

    private static final CategoryMapper MAPPER = CategoryMapper.INSTANCE;

    public Long save(Category category) {
        try {
            var entity = MAPPER.toEntity(category);
            return repo.save(entity).getId();
        } catch (Exception ex) {
            throw new RuntimeException("Невозможно сохранить объект!");
        }
    }

    public Page<Category> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(MAPPER::toDto);
    }
}
