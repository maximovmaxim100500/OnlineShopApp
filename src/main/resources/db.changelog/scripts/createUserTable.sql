-- Создание таблицы для хранения информации о пользователях
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,                 -- Уникальный идентификатор пользователя
    password VARCHAR(255) NOT NULL,           -- Пароль (обязательное поле)
    firstName VARCHAR(10) NOT NULL,           -- Имя пользователя (обязательное поле)
    lastName VARCHAR(10) NOT NULL,            -- Фамилия пользователя (обязательное поле)
    email VARCHAR(255) UNIQUE NOT NULL,       -- Почтовый адрес пользователя (обязательное поле, уникальный)
    phone VARCHAR(255) NOT NULL,               -- Номер телефона пользователя (обязательное поле)
    role TEXT NOT NULL,                       -- Роль пользователя (обязательное поле)
    image VARCHAR(255),                       -- Путь к изображению пользователя
    CONSTRAINT check_role CHECK (role IN ('ADMIN', 'USER')) -- Ограничение для роли пользователя
);