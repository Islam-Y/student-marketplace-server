package com.startit.chatservice.transfer;

import lombok.Data;

@Data
public class Message {
    private Long senderId;
    private Long chatId;
    private String message;
    private Long seqNumber;
    private Long globalSeqNumber;
}
