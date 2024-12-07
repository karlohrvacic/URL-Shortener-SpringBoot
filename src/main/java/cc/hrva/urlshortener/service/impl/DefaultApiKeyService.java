package cc.hrva.urlshortener.service.impl;

import cc.hrva.urlshortener.configuration.properties.AppProperties;
import cc.hrva.urlshortener.converter.ApiKeyUpdateDtoToApiKeyConverter;
import cc.hrva.urlshortener.exception.ApiKeyDoesntExistException;
import cc.hrva.urlshortener.repository.ApiKeyRepository;
import cc.hrva.urlshortener.validator.ApiKeyValidator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import cc.hrva.urlshortener.dto.ApiKeyUpdateDto;
import cc.hrva.urlshortener.model.ApiKey;
import cc.hrva.urlshortener.model.User;
import cc.hrva.urlshortener.service.ApiKeyService;
import cc.hrva.urlshortener.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CommonsLog
@RequiredArgsConstructor
public class DefaultApiKeyService implements ApiKeyService {

    private final UserService userService;
    private final AppProperties appProperties;
    private final ApiKeyValidator apiKeyValidator;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyUpdateDtoToApiKeyConverter apiKeyConverter;

    @Override
    @Transactional
    public ApiKey generateNewApiKey() {
        final var user = userService.getUserFromToken();

        apiKeyValidator.apiKeySlotsAvailable(user);

        return apiKeyRepository.save(new ApiKey(user, appProperties));
    }

    @Override
    public List<ApiKey> fetchMyApiKeys() {
        return userService.getUserFromToken().getApiKeys();
    }

    @Override
    @Transactional
    public ApiKey revokeApiKey(final Long id) {
        final var apiKey = apiKeyRepository.findById(id)
                .orElseThrow(() -> new ApiKeyDoesntExistException("Api key doesn't exist"));

        apiKeyValidator.verifyUserAdminOrOwner(apiKey);
        apiKey.setActive(false);
        return apiKeyRepository.save(apiKey);
    }

    @Override
    @Transactional
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
    @Transactional
    public ApiKey updateKey(final ApiKeyUpdateDto apiKeyUpdateDto) {
        return apiKeyRepository.save(Objects.requireNonNull(apiKeyConverter.convert(apiKeyUpdateDto)));
    }

    @Override
    public void deactivateExpired() {
        final var apiKeys = apiKeyRepository.findByExpirationDateIsLessThanEqualAndActiveTrue(LocalDateTime.now()).stream()
                .map(ApiKey::deactivate)
                .toList();

        apiKeyRepository.saveAll(apiKeys);
        if (!apiKeys.isEmpty()) log.info(String.format("Deactivated %d api keys", apiKeys.size()));
    }

    @Override
    public int getActiveApiKeysCountForUser(final User user) {
        return apiKeyRepository.findByOwnerAndActiveTrue(user).size();
    }

    @Override
    public ApiKey findApiKeyByKey(final String key) {
        return apiKeyRepository.findApiKeyByKey(key)
            .orElseThrow(() -> new ApiKeyDoesntExistException("API key doesn't exist"));
    }

}
