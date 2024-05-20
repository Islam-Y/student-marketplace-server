package com.startit.itemservice.utils;

import com.startit.itemservice.entity.ItemEntity;
import com.startit.itemservice.transfer.SearchFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ItemSpecification {

    public static Specification<ItemEntity> withFilter(SearchFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getItemName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), filter.getItemName() + "%"));
            }

            if (filter.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.join("categories").get("id"), filter.getCategoryId())
                );
            }

            if (filter.getLocationId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("location").get("id"), filter.getLocationId()));
            }

            if (filter.getSellerId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("sellerId"), filter.getSellerId()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
