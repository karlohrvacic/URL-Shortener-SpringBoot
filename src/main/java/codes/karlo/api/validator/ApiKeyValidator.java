package codes.karlo.api.validator;

import codes.karlo.api.model.ApiKey;

public interface ApiKeyValidator {
    void apiKeyExistsByKeyAndIsValid(String key);
    void verifyUserAdminOrOwner(ApiKey apiKey);
}
