package com.startit.itemservice.repository;

import com.startit.itemservice.entity.FeedbackEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FeedbackRepo extends CrudRepository<FeedbackEntity, Long> {

    List<FeedbackEntity> findAllByCustomerId(Long customerId, Pageable pageable);

    @Query(
            value = "SELECT * FROM feedback LEFT JOIN item i ON i.id = feedback.item_id WHERE i.sellerId = ?1",
            nativeQuery = true
    )
    List<FeedbackEntity> findAllByItemSellerId(Long sellerId, Pageable pageable);
}
