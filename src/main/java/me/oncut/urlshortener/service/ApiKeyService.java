package me.oncut.urlshortener.service;

import java.util.List;
import me.oncut.urlshortener.dto.ApiKeyUpdateDto;
import me.oncut.urlshortener.model.ApiKey;
import me.oncut.urlshortener.model.User;

public interface ApiKeyService {

    ApiKey generateNewApiKey();

    List<ApiKey> fetchMyApiKeys();

    ApiKey revokeApiKey(Long id);

    ApiKey apiKeyUseAction(ApiKey apiKey);

    ApiKey fetchApiKeyByKey(String key);

    List<ApiKey> fetchAllApiKeys();

    ApiKey updateKey(ApiKeyUpdateDto apiKeyUpdateDto);

    void deactivateExpired();

    int getActiveApiKeysCountForUser(User user);

    ApiKey findApiKeyByKey(String key);
}
