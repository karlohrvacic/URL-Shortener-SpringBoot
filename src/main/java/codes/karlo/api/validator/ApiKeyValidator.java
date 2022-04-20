package codes.karlo.api.validator;

public interface ApiKeyValidator {
    void apiKeyExistsByKeyAndIsValid(String key);
}
