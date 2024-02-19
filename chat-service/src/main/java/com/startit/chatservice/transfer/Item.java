package com.startit.chatservice.transfer;

import lombok.Data;

import java.util.List;

@Data
public class Item {
    private Long id;
    private String name;
    private Long price;
    private String description;
    private Long statusId;
    private Long locationId;
    private List<Long> categoriesIds;
    private Long sellerId;
}

