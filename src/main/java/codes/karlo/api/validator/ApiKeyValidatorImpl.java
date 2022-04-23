package codes.karlo.api.validator;

import codes.karlo.api.exception.ApiKeyDoesntExistException;
import codes.karlo.api.exception.ApiKeyIsNotValid;
import codes.karlo.api.model.ApiKey;
import codes.karlo.api.model.User;
import codes.karlo.api.repository.ApiKeyRepository;
import codes.karlo.api.service.UserService;
import codes.karlo.api.validator.ApiKeyValidator;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiKeyValidatorImpl implements ApiKeyValidator {

    private final ApiKeyRepository apiKeyRepository;
    private final UserService userService;

    @Override
    public void apiKeyExistsByKeyAndIsValid(final String key) {
        final ApiKey apiKey = apiKeyRepository.findApiKeyByKey(key)
                .orElseThrow(() -> new ApiKeyDoesntExistException("API key doesn't exist"));

        if (apiKey.getApiCallsUsed() >= Optional.ofNullable(apiKey.getApiCallsLimit())
                .orElse(apiKey.getApiCallsUsed()) + 1) {
            throw new ApiKeyIsNotValid("API key exceeded call limit");
        }

        if (LocalDateTime.now().isAfter(Optional.ofNullable(apiKey.getExpirationDate())
                .orElse(LocalDateTime.now().plusDays(1)))) {
            throw new ApiKeyIsNotValid("API key exceeded expiration date");
        }

        if (!apiKey.isActive()) {
            throw new ApiKeyIsNotValid("API key is invalid");
        }
    }

    @Override
    public void verifyUserAdminOrOwner(final ApiKey apiKey) {
        final User currentUser = userService.getUserFromToken();
        final boolean isCurrentUserAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authorities -> authorities.getName().equals("ROLE_ADMIN"));
        if (!apiKey.getOwner().equals(currentUser) && !isCurrentUserAdmin) {
            throw new ApiKeyDoesntExistException("API key doesn't exist");
        }
    }
}
