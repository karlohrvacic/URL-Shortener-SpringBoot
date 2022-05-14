package me.oncut.urlshortener.service;

import me.oncut.urlshortener.dto.ApiKeyUpdateDto;
import me.oncut.urlshortener.model.ApiKey;
import java.util.List;

public interface ApiKeyService {

    ApiKey generateNewApiKey();

    List<ApiKey> fetchMyApiKeys();

    ApiKey revokeApiKey(Long id);

    ApiKey apiKeyUseAction(ApiKey apiKey);

    ApiKey fetchApiKeyByKey(String key);

    List<ApiKey> fetchAllApiKeys();

    ApiKey updateKey(ApiKeyUpdateDto apiKeyUpdateDto);
}
