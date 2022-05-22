package me.oncut.urlshortener.validator.impl;

import me.oncut.urlshortener.exception.EmailExistsException;
import me.oncut.urlshortener.exception.NoAuthorizationException;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import me.oncut.urlshortener.validator.AuthValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthValidatorImpl implements AuthValidator {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void emailUniqueness(final String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailExistsException("Email already exists!");
        }
    }

    @Override
    public void passwordMatchesCurrentPassword(final User user, final String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new NoAuthorizationException("Password doesn't match existing password");
        }
    }
}
