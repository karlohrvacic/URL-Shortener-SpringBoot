package codes.karlo.api.service.impl;

import codes.karlo.api.entity.ApiKey;
import codes.karlo.api.entity.User;
import codes.karlo.api.exception.ApiKeyDoesntExistException;
import codes.karlo.api.exception.UserDoesntExistException;
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
    public ApiKey generateNewApiKey() throws UserDoesntExistException {

        ApiKey apiKey = new ApiKey();

        User user = userService.getUserFromToken();

        apiKey.setApiKey(RandomStringUtils.random(API_KEY_LENGTH, true, true));
        apiKey.setOwner(user);

        user.getApiKeys().add(apiKey);
        userRepository.save(user);

        return apiKey;
    }

    @Override
    public List<ApiKey> fetchMyApiKeys() throws UserDoesntExistException {

        return userService.getUserFromToken().getApiKeys();
    }

    @Override
    public ApiKey revokeApiKey(Long id) throws UserDoesntExistException, ApiKeyDoesntExistException {
        ApiKey apiKey = userService.getUserFromToken()
                .getApiKeys()
                .stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ApiKeyDoesntExistException("Api key doesn't exist"));

        apiKey.setExpirationDate(LocalDateTime.now());
        return apiKeyRepository.save(apiKey);
    }

}
