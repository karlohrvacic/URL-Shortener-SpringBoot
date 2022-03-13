package codes.karlo.api.controller;

import codes.karlo.api.entity.ApiKey;
import codes.karlo.api.exception.ApiKeyDoesntExistException;
import codes.karlo.api.exception.UserDoesntExistException;
import codes.karlo.api.service.ApiKeyService;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/get-my-api-keys")
    public List<ApiKey> fetchMyApiKeys() throws UserDoesntExistException {
        return apiKeyService.fetchMyApiKeys();
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ApiKey> fetchAllApiKeys() {
        return apiKeyService.fetchAllApiKeys();
    }

    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ApiKey> updateApiKey() {

        //TODO
        return apiKeyService.fetchAllApiKeys();
    }

    @GetMapping("/revoke/{id}")
    public ApiKey revokeApiKey(@PathVariable("id") Long id) throws UserDoesntExistException, ApiKeyDoesntExistException {
        return apiKeyService.revokeApiKey(id);
    }

}
