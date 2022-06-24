package me.oncut.urlshortener.validator;

public interface UserValidator {
    void checkEmailUniqueness(String email);
}
