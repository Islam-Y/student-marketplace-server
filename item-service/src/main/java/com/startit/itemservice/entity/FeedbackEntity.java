package com.startit.itemservice.entity;

import com.startit.itemservice.transfer.Mark;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "feedback")
public class FeedbackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Mark mark;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @Column(nullable = false)
    private Long customerId;
}
