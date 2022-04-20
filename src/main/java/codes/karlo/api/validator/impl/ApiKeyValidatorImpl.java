package codes.karlo.api.validator.impl;

import codes.karlo.api.entity.ApiKey;
import codes.karlo.api.exception.ApiKeyIsNotValid;
import codes.karlo.api.repository.ApiKeyRepository;
import codes.karlo.api.validator.ApiKeyValidator;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiKeyValidatorImpl implements ApiKeyValidator {

    private final ApiKeyRepository apiKeyRepository;

    @Override
    public void apiKeyExistsByKeyAndIsValid(final String key) {

        final ApiKey apiKey = apiKeyRepository.findApiKeyByKey(key)
                .orElseThrow(() -> new ApiKeyIsNotValid("API key doesn't exist"));

        if (apiKey.getApiCallsUsed() >= Optional.ofNullable(apiKey.getApiCallsLimit())
                .orElse(apiKey.getApiCallsUsed()) + 1) {
            throw new ApiKeyIsNotValid("API key exceeded call limit");
        }

        if (LocalDateTime.now().isAfter(Optional.ofNullable(apiKey.getExpirationDate())
                .orElse(LocalDateTime.now().plusDays(1)))) {
            throw new ApiKeyIsNotValid("API key exceeded expiration date");
        }

    }
}
