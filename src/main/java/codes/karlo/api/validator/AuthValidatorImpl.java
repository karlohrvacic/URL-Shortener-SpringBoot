package codes.karlo.api.validator;

import codes.karlo.api.exception.EmailExistsException;
import codes.karlo.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
