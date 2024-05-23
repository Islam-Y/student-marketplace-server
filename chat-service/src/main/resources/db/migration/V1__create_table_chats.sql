CREATE TABLE IF NOT EXISTS chats (
    id SERIAL PRIMARY KEY,
    item_id BIGINT,
    customer_id BIGINT
);
