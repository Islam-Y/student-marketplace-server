package com.startit.itemservice.service;

import com.startit.itemservice.entity.ItemEntity;
import com.startit.itemservice.exception.BadDataException;
import com.startit.itemservice.mapper.ItemMapper;
import com.startit.itemservice.mapper.UserMapper;
import com.startit.itemservice.repository.*;
import com.startit.itemservice.transfer.Item;
import com.startit.itemservice.transfer.SearchFilter;
import com.startit.itemservice.utils.ItemSpecification;
import com.startit.shared.transfer.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepo repo;
    private final StatusRepo statusRepo;
    private final LocationRepo locationRepo;
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;

    private static final ItemMapper MAPPER = ItemMapper.INSTANCE;

    public long save(Item item) {
        ItemEntity itemEntity = MAPPER.toEntity(item);

        itemEntity.setStatus( statusRepo.findById(item.getStatusId()).orElseThrow(
                () -> new BadDataException("Статус " + item.getStatusId() + " не найден")
        ));
        itemEntity.setCategories(
                item.getCategoriesIds().stream().map(
                        category -> categoryRepo.findById(category).orElseThrow(
                                () -> new BadDataException("Категория " + category + " не найдена")
                        )
                ).toList()
        );
        itemEntity.setLocation( locationRepo.findById(item.getLocationId()).orElseThrow(
                () -> new BadDataException("Локация " + item.getStatusId() + " не найдена")
        ));

        return repo.save(itemEntity).getId();
    }

    public long update(Item item, String username) {
        long userId = userRepo.findByUsername(username).orElseThrow().getId();
        if (item.getSellerId() != userId) {
            throw new BadDataException("Нельзя обновлять не ваши объявления!");
        }
        return save(item);
    }

    public Item searchItem(Long itemId) {
        return repo.findById(itemId).map(MAPPER::toDto).orElseThrow();
    }

    public List<Item> searchItems(SearchFilter searchFilter, Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("id").ascending()
        );
        return repo.findAll(ItemSpecification.withFilter(searchFilter), sortedPageable).stream()
                .map(MAPPER::toDto)
                .toList();
    }

    @KafkaListener(
            topics = "user-creation",
            containerFactory = "userKafkaListenerContainerFactory",
            groupId = "item")
    public void listenKafka(User user) {
        log.info("Received Message in group item: {} ", user);
        var userEntity = UserMapper.INSTANCE.toEntity(user);
        userRepo.save(userEntity);
    }
}
