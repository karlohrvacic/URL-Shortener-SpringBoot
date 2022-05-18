package me.oncut.urlshortener.validator.impl;

import me.oncut.urlshortener.exception.EmailExistsException;
import me.oncut.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import me.oncut.urlshortener.validator.AuthValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthValidatorImpl implements AuthValidator {

    private final UserRepository userRepository;

    @Override
    public void emailUniqueness(final String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailExistsException("Email already exists!");
        }
    }
}
