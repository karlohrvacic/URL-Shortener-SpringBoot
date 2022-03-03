package codes.karlo.api.controller;

import codes.karlo.api.entity.ApiKey;
import codes.karlo.api.exception.ApiKeyDoesntExistException;
import codes.karlo.api.exception.UserDoesntExistException;
import codes.karlo.api.service.ApiKeyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/key")
@CrossOrigin("${frontend.url}")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @GetMapping("/create")
    public ApiKey generateNewApiKey() throws UserDoesntExistException {
        return apiKeyService.generateNewApiKey();
    }

    @GetMapping
    public List<ApiKey> fetchMyApiKeys() throws UserDoesntExistException {
        return apiKeyService.fetchMyApiKeys();
    }

    @GetMapping("/revoke/{id}")
    public ApiKey fetchMyApiKeys(@PathVariable("id") Long id) throws UserDoesntExistException, ApiKeyDoesntExistException {
        return apiKeyService.revokeApiKey(id);
    }

}
