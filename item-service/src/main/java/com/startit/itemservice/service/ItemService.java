package com.startit.itemservice.service;

import com.startit.itemservice.entity.ItemEntity;
import com.startit.itemservice.mapper.ItemMapper;
import com.startit.itemservice.repository.CategoryRepo;
import com.startit.itemservice.repository.ItemRepo;
import com.startit.itemservice.repository.LocationRepo;
import com.startit.itemservice.repository.StatusRepo;
import com.startit.itemservice.transfer.Item;
import com.startit.itemservice.transfer.SearchFilter;
import com.startit.itemservice.utils.ItemSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepo repo;
    private final StatusRepo statusRepo;
    private final LocationRepo locationRepo;
    private final CategoryRepo categoryRepo;

    private static final ItemMapper MAPPER = ItemMapper.INSTANCE;

    public long save(Item item) {
        ItemEntity itemEntity = MAPPER.toEntity(item);

        itemEntity.setStatus( statusRepo.findById(item.getStatusId()).orElseThrow() );
        itemEntity.setCategories(
                item.getCategoriesIds().stream().map(
                        category -> categoryRepo.findById(category).orElseThrow()
                ).toList()
        );
        itemEntity.setLocation( locationRepo.findById(item.getLocationId()).orElseThrow() );

        return repo.save(itemEntity).getId();
    }

    public Optional<Item> findById(Long id) {
        return repo.findById(id).map(MAPPER::toDto);
    }

    public Page<Item> findBySellerId(Long id, Pageable pageable) {
        return repo.findBySellerId(id, pageable).map(MAPPER::toDto);
    }

    public List<Item> searchItems(SearchFilter searchFilter, Pageable pageable) {
        return repo.findAll(ItemSpecification.withFilter(searchFilter), pageable).stream()
                .map(MAPPER::toDto)
                .toList();
    }

    public List<Item> getAll(Pageable pageable) {
        return repo.findAll(pageable).stream()
                .map(MAPPER::toDto)
                .toList();
    }
}
