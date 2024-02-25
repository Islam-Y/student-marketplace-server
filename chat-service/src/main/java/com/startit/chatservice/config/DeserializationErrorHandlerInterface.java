package com.startit.chatservice.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface DeserializationErrorHandlerInterface {
    void handleDeserializationError(Exception exception, ConsumerRecord record);
}
