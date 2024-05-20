package com.startit.itemservice.service;

import com.startit.itemservice.entity.FeedbackEntity;
import com.startit.itemservice.exception.BadDataException;
import com.startit.itemservice.mapper.FeedbackMapper;
import com.startit.itemservice.mapper.UserMapper;
import com.startit.itemservice.repository.FeedbackRepo;
import com.startit.itemservice.repository.ItemRepo;
import com.startit.itemservice.repository.UserRepo;
import com.startit.itemservice.transfer.Feedback;
import com.startit.shared.transfer.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FeedbackService {

    private final FeedbackRepo repo;

    private final ItemRepo itemRepo;

    private final UserRepo userRepo;

    private static final FeedbackMapper MAPPER = FeedbackMapper.INSTANCE;

    public Feedback save(String username, Feedback feedback) {
        var entity = new FeedbackEntity();
        entity.setMark(feedback.getMark());
        entity.setText(feedback.getText());
        entity.setItem(
                itemRepo.findById(feedback.getItemId()).orElseThrow());
        entity.setCustomerId(
                userRepo.findByUsername(username).orElseThrow().getId()
        );

        if (entity.getItem().getSellerId().equals(entity.getCustomerId()))
            throw new BadDataException("Продавец не может оставить отзыв на собственный предмет!");

        return MAPPER.toDto(
                repo.save(entity));
    }

    public List<Feedback> getSellerFeedback(Long userId, Pageable pageable) {
        return repo.findAllByItemSellerId(userId, pageable).stream()
                .map(MAPPER::toDto)
                .toList();
    }

    public List<Feedback> getCustomerFeedback(Long userId, Pageable pageable) {
        return repo.findAllByCustomerId(userId, pageable).stream()
                .map(MAPPER::toDto)
                .toList();
    }
}
