package cc.hrva.urlshortener.validator;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import cc.hrva.urlshortener.exception.ApiKeyDoesntExistException;
import cc.hrva.urlshortener.exception.ApiKeyIsNotValid;
import cc.hrva.urlshortener.model.ApiKey;
import cc.hrva.urlshortener.model.User;
import cc.hrva.urlshortener.model.codebook.Authorities;
import cc.hrva.urlshortener.repository.ApiKeyRepository;
import cc.hrva.urlshortener.service.UserService;
import cc.hrva.urlshortener.validator.impl.DefaultApiKeyValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultApiKeyValidatorTest {

    private ApiKeyValidator apiKeyValidator;

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @Mock
    private UserService userService;

    private ApiKey apiKey;

    @BeforeEach
    void setUp() {
        this.apiKeyValidator = new DefaultApiKeyValidator(
                this.userService,
                this.apiKeyRepository
        );

        this.apiKey = ApiKey.builder()
                .apiCallsLimit(100L)
                .apiCallsUsed(0L)
                .createDate(LocalDateTime.now())
                .expirationDate(LocalDateTime.now().plusDays(1L))
                .active(true)
                .build();
    }

    @Test
    void shouldVerifyUserAdminOrOwner() {
        final Authorities authorities = Authorities.builder().id(1L).name("ROLE_ADMIN").build();
        final User user = User.builder().authorities(Collections.singletonList(authorities)).build();
        apiKey.setOwner(user);

        when(userService.getUserFromToken()).thenReturn(user);

        assertThatCode(() -> apiKeyValidator.verifyUserAdminOrOwner(apiKey))
                .doesNotThrowAnyException();
    }

    void shouldFailVerifyUserAdminOrOwner() {
        final Authorities authorities = Authorities.builder().id(1L).name("ROLE_USER").build();
        final User user = User.builder().authorities(Collections.singletonList(authorities)).build();
        apiKey.setOwner(User.builder().build());

        when(userService.getUserFromToken()).thenReturn(user);

        assertThatThrownBy(() -> apiKeyValidator.verifyUserAdminOrOwner(apiKey))
                .isInstanceOf(ApiKeyDoesntExistException.class)
                .hasMessage("API key doesn't exist");
    }

    @Test
    void shouldApiKeyExistsByKeyAndIsValid() {
        final String key = "test";

        when(apiKeyRepository.findApiKeyByKey(key)).thenReturn(Optional.ofNullable(apiKey));

        assertThatCode(() -> apiKeyValidator.apiKeyExistsByKeyAndIsValid(key))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldFailApiKeyExistsByKeyAndIsValidApiKeyDoesntExist() {
        final String key = "test";

        when(apiKeyRepository.findApiKeyByKey(key)).thenReturn(Optional.empty());

        assertThatCode(() -> apiKeyValidator.apiKeyExistsByKeyAndIsValid(key))
                .isInstanceOf(ApiKeyDoesntExistException.class)
                .hasMessage("API key doesn't exist");
    }

    @Test
    void shouldFailApiKeyExistsByKeyAndIsValidExceededCallLimit() {
        final String key = "test";

        apiKey.setApiCallsUsed(101L);
        when(apiKeyRepository.findApiKeyByKey(key)).thenReturn(Optional.ofNullable(apiKey));

        assertThatCode(() -> apiKeyValidator.apiKeyExistsByKeyAndIsValid(key))
                .isInstanceOf(ApiKeyIsNotValid.class)
                .hasMessage("API key exceeded call limit");
    }

    @Test
    void shouldFailApiKeyExistsByKeyAndIsValidExceededExpirationDate() {
        final String key = "test";

        apiKey.setExpirationDate(LocalDateTime.now().minusDays(1));
        when(apiKeyRepository.findApiKeyByKey(key)).thenReturn(Optional.ofNullable(apiKey));

        assertThatCode(() -> apiKeyValidator.apiKeyExistsByKeyAndIsValid(key))
                .isInstanceOf(ApiKeyIsNotValid.class)
                .hasMessage("API key exceeded expiration date");
    }

    @Test
    void shouldFailApiKeyExistsByKeyAndIsValidApiNotValid() {
        final String key = "test";

        apiKey.setActive(false);
        when(apiKeyRepository.findApiKeyByKey(key)).thenReturn(Optional.ofNullable(apiKey));

        assertThatCode(() -> apiKeyValidator.apiKeyExistsByKeyAndIsValid(key))
                .isInstanceOf(ApiKeyIsNotValid.class)
                .hasMessage("API key is invalid");
    }
}