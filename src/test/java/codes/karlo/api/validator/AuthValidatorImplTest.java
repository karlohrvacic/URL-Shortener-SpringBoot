package codes.karlo.api.validator;

import codes.karlo.api.exception.EmailExistsException;
import codes.karlo.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthValidatorImplTest {

    private AuthValidator authValidator;

    @Mock
    private UserRepository userRepository;

    private String email;

    @BeforeEach
    void setUp() {
        this.authValidator = new AuthValidatorImpl(userRepository);

        this.email = "email";
    }

    @Test
    void shouldValidateEmailUniqueness() {
        when(userRepository.existsByEmail(email)).thenReturn(false);
        assertThatCode(() -> authValidator.emailUniqueness(email))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldFailValidateEmailUniquenessEmailExists() {
        when(userRepository.existsByEmail(email)).thenReturn(true);
        assertThatThrownBy(() -> authValidator.emailUniqueness(email))
                .isInstanceOf(EmailExistsException.class)
                .hasMessage("Email already exists!");
    }
}