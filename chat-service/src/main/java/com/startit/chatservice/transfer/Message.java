package com.startit.chatservice.transfer;

import lombok.Data;

@Data
public class Message {
    private Long senderId;
    private String message;
    private Long seqNumber;
}
