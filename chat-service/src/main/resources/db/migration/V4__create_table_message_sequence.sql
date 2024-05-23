CREATE TABLE IF NOT EXISTS message_sequence (
    id SERIAL PRIMARY KEY,
    message_id BIGINT,
    user_id BIGINT,
    seq_number BIGINT,
    FOREIGN KEY (message_id) REFERENCES messages(id),
    FOREIGN KEY (user_id) REFERENCES users_chat_ms(id)
);
