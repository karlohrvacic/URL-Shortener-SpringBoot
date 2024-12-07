package cc.hrva.urlshortener.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import cc.hrva.urlshortener.configuration.properties.AppProperties;
import cc.hrva.urlshortener.converter.ApiKeyUpdateDtoToApiKeyConverter;
import cc.hrva.urlshortener.dto.ApiKeyUpdateDto;
import cc.hrva.urlshortener.exception.ApiKeyDoesntExistException;
import cc.hrva.urlshortener.model.ApiKey;
import cc.hrva.urlshortener.model.User;
import cc.hrva.urlshortener.repository.ApiKeyRepository;
import cc.hrva.urlshortener.service.impl.DefaultApiKeyService;
import cc.hrva.urlshortener.validator.ApiKeyValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiKeyServiceTest {

    private ApiKeyService apiKeyService;

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @Mock
    private UserService userService;

    @Mock
    private AppProperties appProperties;

    @Mock
    private ApiKeyValidator apiKeyValidator;

    @Mock
    private ApiKeyUpdateDtoToApiKeyConverter apiKeyConverter;

    @BeforeEach
    void setUp() {
        this.apiKeyService = new DefaultApiKeyService(
                userService,
                appProperties,
                apiKeyValidator,
                apiKeyRepository,
                apiKeyConverter);
    }

    @Test
    void shouldGenerateNewApiKey() {
        final User user = User.builder().build();
        final ApiKey apiKey = ApiKey.builder().build();

        when(userService.getUserFromToken()).thenReturn(user);
        when(apiKeyRepository.save(any(ApiKey.class))).thenReturn(apiKey);

        assertThat(apiKeyService.generateNewApiKey()).isEqualTo(apiKey);
    }

    @Test
    void shouldFetchMyApiKeys() {
        final ApiKey apiKey = ApiKey.builder().build();
        final List<ApiKey> apiKeyList = Collections.singletonList(apiKey);
        final User user = User.builder().apiKeys(apiKeyList).build();

        when(userService.getUserFromToken()).thenReturn(user);

        assertThat(apiKeyService.fetchMyApiKeys()).isEqualTo(apiKeyList);
    }

    @Test
    void shouldFailRevokeApiKey() {
        final Long id = 1L;

        when(apiKeyRepository.findById(id)).thenReturn(Optional.empty());

        assertThatCode(() -> apiKeyService.revokeApiKey(id))
                .isInstanceOf(ApiKeyDoesntExistException.class)
                .hasMessage("Api key doesn't exist");
    }

    @Test
    void shouldRevokeApiKey() {
        final Long id = 1L;
        final ApiKey apiKey = ApiKey.builder().active(false).build();

        when(apiKeyRepository.findById(id)).thenReturn(Optional.ofNullable(apiKey));
        when(apiKeyRepository.save(any(ApiKey.class))).thenReturn(apiKey);

        assertThat(apiKeyService.revokeApiKey(id)).isEqualTo(apiKey);
    }

    @Test
    void shouldApiKeyUseAction() {
        final ApiKey apiKey = ApiKey.builder().apiCallsUsed(1L).build();
        final ApiKey apiKeyResult = ApiKey.builder().apiCallsUsed(2L).build();

        when(apiKeyRepository.save(any(ApiKey.class))).thenReturn(apiKey);
        assertThat(apiKeyService.apiKeyUseAction(apiKey)).usingRecursiveComparison().isEqualTo(apiKeyResult);
    }

    @Test
    void shouldFetchApiKeyByKey() {
        final ApiKey apiKey = ApiKey.builder().apiCallsUsed(1L).build();
        final String key = "key";
        when(apiKeyRepository.findApiKeyByKey(key)).thenReturn(Optional.ofNullable(apiKey));

        assertThat(apiKeyService.fetchApiKeyByKey(key)).usingRecursiveComparison().isEqualTo(apiKey);
    }

    @Test
    void shouldFailFetchApiKeyByKey() {
        final String key = "key";

        when(apiKeyRepository.findApiKeyByKey(key)).thenReturn(Optional.empty());

        assertThatCode(() -> apiKeyService.fetchApiKeyByKey(key))
                .isInstanceOf(ApiKeyDoesntExistException.class)
                .hasMessage("Sent API key doesn't exist");
    }

    @Test
    void shouldFetchAllApiKeys() {
        final ApiKey apiKey = ApiKey.builder().build();
        final List<ApiKey> apiKeyList = Collections.singletonList(apiKey);

        when(apiKeyRepository.findAll()).thenReturn(apiKeyList);

        assertThat(apiKeyService.fetchAllApiKeys()).isEqualTo(apiKeyList);
    }

    @Test
    void shouldUpdateKey() {
        final ApiKey apiKey = ApiKey.builder().build();
        final ApiKeyUpdateDto apiKeyUpdateDto = ApiKeyUpdateDto.builder().build();

        when(apiKeyConverter.convert(apiKeyUpdateDto)).thenReturn(apiKey);
        when(apiKeyRepository.save(apiKey)).thenReturn(apiKey);

        assertThat(apiKeyService.updateKey(apiKeyUpdateDto)).isEqualTo(apiKey);
    }

}