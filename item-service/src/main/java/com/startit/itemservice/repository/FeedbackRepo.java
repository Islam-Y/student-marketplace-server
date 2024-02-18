package com.startit.itemservice.repository;

import com.startit.itemservice.entity.FeedbackEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FeedbackRepo extends CrudRepository<FeedbackEntity, Long> {

    List<FeedbackEntity> findAllByCustomer_Id(Long customerId, Pageable pageable);

    @Query(
            value = "SELECT * FROM feedback LEFT JOIN item i ON i.id = feedback.item_id WHERE i.seller_id = ?1",
            nativeQuery = true
    )
    List<FeedbackEntity> findAllByItemSeller_Id(Long sellerId, Pageable pageable);
}
