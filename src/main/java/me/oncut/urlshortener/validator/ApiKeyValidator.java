package me.oncut.urlshortener.validator;

import me.oncut.urlshortener.model.ApiKey;

public interface ApiKeyValidator {
    void apiKeyExistsByKeyAndIsValid(String key);

    void verifyUserAdminOrOwner(ApiKey apiKey);

    void apiKeySlotsAvailable();
}
