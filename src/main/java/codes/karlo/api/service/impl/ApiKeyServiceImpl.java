package codes.karlo.api.service.impl;

import codes.karlo.api.config.AppProperties;
import codes.karlo.api.converter.ApiKeyUpdateDtoToApiKeyConverter;
import codes.karlo.api.dto.ApiKeyUpdateDto;
import codes.karlo.api.exception.ApiKeyDoesntExistException;
import codes.karlo.api.model.ApiKey;
import codes.karlo.api.model.User;
import codes.karlo.api.repository.ApiKeyRepository;
import codes.karlo.api.service.ApiKeyService;
import codes.karlo.api.service.UserService;
import codes.karlo.api.validator.ApiKeyValidator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final UserService userService;
    private final AppProperties appProperties;
    private final ApiKeyValidator apiKeyValidator;

    private final ApiKeyUpdateDtoToApiKeyConverter apiKeyConverter;

    @Override
    public ApiKey generateNewApiKey() {

        final ApiKey apiKey = new ApiKey();
        final User user = userService.getUserFromToken();

        apiKey.setKey(RandomStringUtils.random(Math.toIntExact(appProperties.getApiKeyLength()), true, true));
        apiKey.setOwner(user);
        apiKey.setApiCallsLimit(appProperties.getApiKeyCallsLimit());
        apiKey.setExpirationDate(LocalDateTime.now().plusMonths(appProperties.getApiKeyExpirationInMonths()));

        return apiKeyRepository.save(apiKey);
    }

    @Override
    public List<ApiKey> fetchMyApiKeys() {
        return userService.getUserFromToken().getApiKeys();
    }

    @Override
    public ApiKey revokeApiKey(final Long id) {
        final ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(() -> new ApiKeyDoesntExistException("Api key doesn't exist"));

        apiKeyValidator.verifyUserAdminOrOwner(apiKey);
        apiKey.setActive(false);
        return apiKeyRepository.save(apiKey);
    }

    @Override
    public ApiKey apiKeyUseAction(final ApiKey apiKey) {
        return apiKeyRepository.save(apiKey.apiKeyUsed());
    }

    @Override
    public ApiKey fetchApiKeyByKey(final String key) {
        return apiKeyRepository.findApiKeyByKey(key)
                .orElseThrow(() -> new ApiKeyDoesntExistException("Sent API key doesn't exist"));
    }

    @Override
    public List<ApiKey> fetchAllApiKeys() {
        return apiKeyRepository.findAll();
    }

    @Override
    public ApiKey updateKey(final ApiKeyUpdateDto apiKeyUpdateDto) {
        return apiKeyRepository.save(Objects.requireNonNull(apiKeyConverter.convert(apiKeyUpdateDto)));
    }

}
