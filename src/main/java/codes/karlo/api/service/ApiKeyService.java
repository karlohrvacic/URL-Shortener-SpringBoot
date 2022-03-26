package codes.karlo.api.service;

import codes.karlo.api.dto.ApiKeyUpdateDto;
import codes.karlo.api.entity.ApiKey;

import java.util.List;

public interface ApiKeyService {

    ApiKey generateNewApiKey();

    List<ApiKey> fetchMyApiKeys();

    ApiKey revokeApiKey(Long id);

    void apiKeyUseAction(ApiKey apiKey);

    ApiKey fetchApiKeyByKey(String key);

    List<ApiKey> fetchAllApiKeys();

    List<ApiKey> updateKey(ApiKeyUpdateDto apiKeyUpdateDto);
}
