package me.oncut.urlshortener.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.config.AppProperties;
import me.oncut.urlshortener.converter.ApiKeyUpdateDtoToApiKeyConverter;
import me.oncut.urlshortener.dto.ApiKeyUpdateDto;
import me.oncut.urlshortener.exception.ApiKeyDoesntExistException;
import me.oncut.urlshortener.model.ApiKey;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.repository.ApiKeyRepository;
import me.oncut.urlshortener.service.ApiKeyService;
import me.oncut.urlshortener.service.UserService;
import me.oncut.urlshortener.validator.ApiKeyValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CommonsLog
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final UserService userService;
    private final AppProperties appProperties;
    private final ApiKeyValidator apiKeyValidator;

    private final ApiKeyUpdateDtoToApiKeyConverter apiKeyConverter;

    @Override
    @Transactional
    public ApiKey generateNewApiKey() {
        final ApiKey apiKey = new ApiKey();
        final User user = userService.getUserFromToken();

        apiKeyValidator.apiKeySlotsAvailable();

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
    @Transactional
    public ApiKey revokeApiKey(final Long id) {
        final ApiKey apiKey = apiKeyRepository.findById(id)
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
        final List<ApiKey> apiKeys = apiKeyRepository.findByExpirationDateIsLessThanEqualAndActiveTrue(LocalDateTime.now()).stream()
                .map(apiKey -> {
                    apiKey.setActive(false);
                    return apiKey;
                })
                .toList();

        apiKeyRepository.saveAll(apiKeys);
        if (!apiKeys.isEmpty()) log.info(String.format("Deactivated %d api keys", apiKeys.size()));
    }

}
