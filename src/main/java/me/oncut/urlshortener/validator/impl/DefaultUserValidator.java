package me.oncut.urlshortener.validator.impl;

import lombok.RequiredArgsConstructor;
import me.oncut.urlshortener.exception.EmailExistsException;
import me.oncut.urlshortener.repository.UserRepository;
import me.oncut.urlshortener.validator.UserValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultUserValidator implements UserValidator {

    private final UserRepository userRepository;

    @Override
    public void checkEmailUniqueness(final String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailExistsException("Email already exists!");
        }
    }

}
