--Создание таблицы Comments
CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    text TEXT NOT NULL,
    ads_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (ads_id) REFERENCES ads(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);