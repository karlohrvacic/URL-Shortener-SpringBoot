package codes.karlo.api.controller;

import codes.karlo.api.dto.ApiKeyUpdateDto;
import codes.karlo.api.model.ApiKey;
import codes.karlo.api.exception.ApiKeyDoesntExistException;
import codes.karlo.api.exception.UserDoesntExistException;
import codes.karlo.api.service.ApiKeyService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/key")
@CrossOrigin("${app.frontend-url}")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

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
    public ApiKey updateApiKey(@Valid @RequestBody final ApiKeyUpdateDto apiKeyUpdateDto) {
        return apiKeyService.updateKey(apiKeyUpdateDto);
    }

    @GetMapping("/revoke/{id}")
    public ApiKey revokeApiKey(@PathVariable("id") final Long id) throws UserDoesntExistException, ApiKeyDoesntExistException {
        return apiKeyService.revokeApiKey(id);
    }

}
