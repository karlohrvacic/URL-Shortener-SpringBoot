package me.oncut.urlshortener.validator;

import me.oncut.urlshortener.exception.EmailExistsException;
import me.oncut.urlshortener.repository.UserRepository;
import me.oncut.urlshortener.validator.impl.DefaultUserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultUserValidatorShould {

    private UserValidator userValidator;

    @Mock
    private UserRepository userRepository;

    private String email;

    @BeforeEach
    void setUp() {
        this.userValidator = new DefaultUserValidator(userRepository);

        this.email = "email";
    }

    @Test
    void validateEmailUniqueness() {
        when(userRepository.existsByEmail(email)).thenReturn(false);
        assertThatCode(() -> userValidator.checkEmailUniqueness(email))
                .doesNotThrowAnyException();
    }

    @Test
    void failValidateEmailUniquenessBecauseEmailExists() {
        when(userRepository.existsByEmail(email)).thenReturn(true);
        assertThatThrownBy(() -> userValidator.checkEmailUniqueness(email))
                .isInstanceOf(EmailExistsException.class)
                .hasMessage("Email already exists!");
    }

}