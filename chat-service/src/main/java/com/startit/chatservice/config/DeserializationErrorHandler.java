package com.startit.chatservice.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

@Slf4j
public class DeserializationErrorHandler implements CommonErrorHandler {

    @Override
    public boolean handleOne(Exception exception, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer, MessageListenerContainer container) {
        if (exception instanceof ClassCastException) {
            log.error("Deserialization error (type mismatch): {}. Skipping message.", record, exception);
        } else {
            // Log and handle other deserialization errors appropriately
            log.error("Deserialization error: {}. Skipping message.", record, exception);
        }
        return true;
    }

    @Override
    public void handleOtherException(Exception exception, Consumer<?, ?> consumer, MessageListenerContainer container, boolean batchListener) {
        if (exception instanceof ClassCastException) {
            log.error("Deserialization error (type mismatch): {}. Skipping message.", exception.getMessage());
        } else {
            // Log and handle other deserialization errors appropriately
            log.error("Deserialization error: {}. Skipping message.", exception.getMessage());
        }
    }
}


