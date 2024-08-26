package ru.skypro.homework.exception;

public class UserWithIdNotFoundException extends RuntimeException {
    public UserWithIdNotFoundException(String message, Long id) {
        super(message);
    }
}
