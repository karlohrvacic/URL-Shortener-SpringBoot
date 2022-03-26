package codes.karlo.api.service.impl;

import codes.karlo.api.entity.ApiKey;
import codes.karlo.api.entity.User;
import codes.karlo.api.exception.ApiKeyDoesntExistException;
import codes.karlo.api.repository.ApiKeyRepository;
import codes.karlo.api.repository.UserRepository;
import codes.karlo.api.service.ApiKeyService;
import codes.karlo.api.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${api.key-length}")
    private int API_KEY_LENGTH;

    @Autowired
    public ApiKeyServiceImpl(ApiKeyRepository apiKeyRepository, UserRepository userRepository, UserService userService) {
        this.apiKeyRepository = apiKeyRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public ApiKey generateNewApiKey() {

        ApiKey apiKey = new ApiKey();

        User user = userService.getUserFromToken();

        apiKey.setKey(RandomStringUtils.random(API_KEY_LENGTH, true, true));
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
    public ApiKey revokeApiKey(Long id) {
        ApiKey apiKey = userService.getUserFromToken()
                .getApiKeys()
                .stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ApiKeyDoesntExistException("Api key doesn't exist"));

        apiKey.setExpirationDate(LocalDateTime.now());
        return apiKeyRepository.save(apiKey);
    }

    @Override
    public ApiKey apiKeyUseAction(ApiKey apiKey) {

        apiKey.setApiCallsUsed(apiKey.getApiCallsUsed() + 1);

        return apiKeyRepository.save(apiKey);
    }

    @Override
    public ApiKey fetchApiKeyByKey(String key) {
        System.out.println(apiKeyRepository.findApiKeyByKey(key));
        return apiKeyRepository.findApiKeyByKey(key)
                .orElseThrow(() -> new ApiKeyDoesntExistException("Sent API key doesn't exist"));
    }

    @Override
    public List<ApiKey> fetchAllApiKeys() {
        return apiKeyRepository.findAll();
    }

}
