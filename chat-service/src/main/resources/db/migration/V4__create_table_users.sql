CREATE TABLE IF NOT EXISTS users_chat_ms (
    id BIGINT PRIMARY KEY,
    family_name VARCHAR(255),
    isu_number BIGINT UNIQUE,
    name VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255),
    username VARCHAR(255) UNIQUE
);
