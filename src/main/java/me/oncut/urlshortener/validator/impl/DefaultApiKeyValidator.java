package me.oncut.urlshortener.validator.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.oncut.urlshortener.exception.ApiKeyDoesntExistException;
import me.oncut.urlshortener.exception.ApiKeyIsNotValid;
import me.oncut.urlshortener.exception.ApiKeySlotException;
import me.oncut.urlshortener.exception.NoAuthorizationException;
import me.oncut.urlshortener.model.ApiKey;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.repository.ApiKeyRepository;
import me.oncut.urlshortener.service.UserService;
import me.oncut.urlshortener.validator.ApiKeyValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultApiKeyValidator implements ApiKeyValidator {

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
            throw new NoAuthorizationException("You don't have authorization for this action");
        }
    }

    @Override
    public void apiKeySlotsAvailable(final User user) {
        if (user.getApiKeySlots() <= apiKeyRepository.findByOwnerAndActiveTrue(user).size()) {
            throw new ApiKeySlotException("Can't create new API key as it exceeds API key slot limit. Contact admin for bigger slot.");
        }
    }

}
