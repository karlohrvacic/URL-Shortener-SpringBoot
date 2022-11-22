package cc.hrva.urlshortener.validator;

import cc.hrva.urlshortener.model.ApiKey;
import cc.hrva.urlshortener.model.User;

public interface ApiKeyValidator {
    void apiKeyExistsByKeyAndIsValid(String key);

    void verifyUserAdminOrOwner(ApiKey apiKey);

    void apiKeySlotsAvailable(User user);
}
