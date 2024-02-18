package com.startit.itemservice.service;

import com.startit.itemservice.entity.FeedbackEntity;
import com.startit.itemservice.exception.BadDataException;
import com.startit.itemservice.mapper.FeedbackMapper;
import com.startit.itemservice.repository.FeedbackRepo;
import com.startit.itemservice.repository.ItemRepo;
import com.startit.itemservice.transfer.Feedback;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FeedbackService {

    private final FeedbackRepo repo;

    private final ItemRepo itemRepo;

    private final UserServiceClient userServiceClient;

    private static final FeedbackMapper MAPPER = FeedbackMapper.INSTANCE;

    public Feedback save(String username, Feedback feedback) {
        if (!userServiceClient.userExists(feedback.getCustomerId()))
            throw new BadDataException("Пользователь не найден!");
        var entity = new FeedbackEntity();
        entity.setMark(feedback.getMark());
        entity.setText(feedback.getText());
        entity.setItem(
                itemRepo.findById(feedback.getItemId()).orElseThrow());
        entity.setCustomerId(feedback.getCustomerId());

        if (userServiceClient.getUser(entity.getItem().getSellerId()).getUsername().equals(username))
            throw new BadDataException("Продавец не может оставить отзыв на собственный предмет!");

        return MAPPER.toDto(
                repo.save(entity));
    }

    public List<Feedback> getSellerFeedback(Long userId, Pageable pageable) {
        return repo.findAllByItemSeller_Id(userId, pageable).stream()
                .map(MAPPER::toDto)
                .toList();
    }

    public List<Feedback> getCustomerFeedback(Long userId, Pageable pageable) {
        return repo.findAllByCustomer_Id(userId, pageable).stream()
                .map(MAPPER::toDto)
                .toList();
    }
}
