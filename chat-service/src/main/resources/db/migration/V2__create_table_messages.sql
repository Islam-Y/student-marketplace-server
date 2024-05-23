CREATE TABLE IF NOT EXISTS messages (
    id SERIAL PRIMARY KEY,
    chat_id BIGINT,
    sender_id BIGINT,
    message VARCHAR(255),
    FOREIGN KEY (chat_id) REFERENCES chats(id)
);
