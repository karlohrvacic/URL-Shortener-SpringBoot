package cc.hrva.urlshortener.service;

import java.util.List;
import cc.hrva.urlshortener.dto.ApiKeyUpdateDto;
import cc.hrva.urlshortener.model.ApiKey;
import cc.hrva.urlshortener.model.User;

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
