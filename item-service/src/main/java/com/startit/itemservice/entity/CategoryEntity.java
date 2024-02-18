package com.startit.itemservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;
}
