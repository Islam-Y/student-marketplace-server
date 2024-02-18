package com.startit.itemservice.transfer;

import lombok.Data;

@Data
public class SearchFilter {
    String itemName;
    Long categoryId;
    Long locationId;
    Long sellerId;
}
