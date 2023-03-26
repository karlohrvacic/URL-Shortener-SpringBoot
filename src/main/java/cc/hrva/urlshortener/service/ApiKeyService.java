package cc.hrva.urlshortener.service;

import java.util.List;
import cc.hrva.urlshortener.dto.ApiKeyUpdateDto;
import cc.hrva.urlshortener.model.ApiKey;
import cc.hrva.urlshortener.model.User;

public interface ApiKeyService {

    void deactivateExpired();
    ApiKey generateNewApiKey();
    ApiKey revokeApiKey(Long id);
    List<ApiKey> fetchMyApiKeys();
    List<ApiKey> fetchAllApiKeys();
    ApiKey findApiKeyByKey(String key);
    ApiKey fetchApiKeyByKey(String key);
    ApiKey apiKeyUseAction(ApiKey apiKey);
    int getActiveApiKeysCountForUser(User user);
    ApiKey updateKey(ApiKeyUpdateDto apiKeyUpdateDto);

}
