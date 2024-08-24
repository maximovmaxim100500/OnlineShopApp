-- Создание таблицы ads
CREATE TABLE IF NOT EXISTS ads (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price BIGINT NOT NULL,
    description TEXT NOT NULL,
    image_url VARCHAR(255),
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);