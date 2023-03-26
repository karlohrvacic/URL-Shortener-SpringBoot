package cc.hrva.urlshortener.validator;

import cc.hrva.urlshortener.model.ApiKey;
import cc.hrva.urlshortener.model.User;

public interface ApiKeyValidator {

    void apiKeySlotsAvailable(User user);
    void verifyUserAdminOrOwner(ApiKey apiKey);
    void apiKeyExistsByKeyAndIsValid(String key);

}
