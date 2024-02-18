package com.startit.itemservice.transfer;

import lombok.Data;

@Data
public class Feedback {
    private Mark mark;
    private String text;
    private Long itemId;
    private Long customerId;
}
