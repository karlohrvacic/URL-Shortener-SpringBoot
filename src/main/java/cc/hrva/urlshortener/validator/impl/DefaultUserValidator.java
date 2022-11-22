package cc.hrva.urlshortener.validator.impl;

import cc.hrva.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import cc.hrva.urlshortener.exception.EmailExistsException;
import cc.hrva.urlshortener.validator.UserValidator;
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
