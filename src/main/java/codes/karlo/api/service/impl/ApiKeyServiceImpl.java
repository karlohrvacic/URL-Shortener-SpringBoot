package codes.karlo.api.service.impl;

import codes.karlo.api.config.AppProperties;
import codes.karlo.api.dto.ApiKeyUpdateDto;
import codes.karlo.api.entity.ApiKey;
import codes.karlo.api.entity.User;
import codes.karlo.api.exception.ApiKeyDoesntExistException;
import codes.karlo.api.repository.ApiKeyRepository;
import codes.karlo.api.repository.UserRepository;
import codes.karlo.api.service.ApiKeyService;
import codes.karlo.api.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;
    private final UserService userService;

   private final AppProperties appProperties;

    @Override
    public ApiKey generateNewApiKey() {

        final ApiKey apiKey = new ApiKey();

        final User user = userService.getUserFromToken();

        apiKey.setKey(RandomStringUtils.random(appProperties.getApiKeyLength(), true, true));
        apiKey.setOwner(user);

        user.getApiKeys().add(apiKey);
        userRepository.save(user);

        return apiKey;
    }

    @Override
    public List<ApiKey> fetchMyApiKeys() {

        return userService.getUserFromToken().getApiKeys();
    }

    @Override
    public ApiKey revokeApiKey(final Long id) {
        final ApiKey apiKey = userService.getUserFromToken()
                .getApiKeys()
                .stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ApiKeyDoesntExistException("Api key doesn't exist"));

        apiKey.setExpirationDate(LocalDateTime.now());
        return apiKeyRepository.save(apiKey);
    }

    @Override
    public ApiKey apiKeyUseAction(final ApiKey apiKey) {

        apiKey.setApiCallsUsed(apiKey.getApiCallsUsed() + 1);

        return apiKeyRepository.save(apiKey);
    }

    @Override
    public ApiKey fetchApiKeyByKey(final String key) {
        return apiKeyRepository.findApiKeyByKey(key)
                .orElseThrow(() -> new ApiKeyDoesntExistException("Sent API key doesn't exist"));
    }

    @Override
    public List<ApiKey> fetchAllApiKeys() {
        return apiKeyRepository.findAll();
    }

    @Override
    public List<ApiKey> updateKey(final ApiKeyUpdateDto apiKeyUpdateDto) {
        //todo
        return null;
    }

}
