package me.oncut.urlshortener.converter;

import me.oncut.urlshortener.dto.ApiKeyUpdateDto;
import me.oncut.urlshortener.exception.ApiKeyDoesntExistException;
import me.oncut.urlshortener.model.ApiKey;
import me.oncut.urlshortener.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiKeyUpdateDtoToApiKeyConverter implements Converter<ApiKeyUpdateDto, ApiKey> {

    private final ApiKeyRepository apiKeyRepository;

    @Override
    public ApiKey convert(final ApiKeyUpdateDto apiKeyUpdateDto) {
        final ApiKey existingApiKey = apiKeyRepository.findById(apiKeyUpdateDto.getId())
                .orElseThrow(() -> new ApiKeyDoesntExistException(String.format("API key with ID %d has not been found",
                        apiKeyUpdateDto.getId())));
        existingApiKey.setApiCallsLimit(apiKeyUpdateDto.getApiCallsLimit());
        existingApiKey.setApiCallsUsed(apiKeyUpdateDto.getApiCallsUsed());
        existingApiKey.setExpirationDate(apiKeyUpdateDto.getExpirationDate());
        existingApiKey.setActive(apiKeyUpdateDto.getActive());
        return existingApiKey;
    }

}
