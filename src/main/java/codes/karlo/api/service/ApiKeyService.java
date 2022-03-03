package codes.karlo.api.service;

import codes.karlo.api.entity.ApiKey;
import codes.karlo.api.exception.ApiKeyDoesntExistException;
import codes.karlo.api.exception.UserDoesntExistException;

import java.util.List;

public interface ApiKeyService {

    ApiKey generateNewApiKey() throws UserDoesntExistException;

    List<ApiKey> fetchMyApiKeys() throws UserDoesntExistException;

    ApiKey revokeApiKey(Long id) throws UserDoesntExistException, ApiKeyDoesntExistException;

}
