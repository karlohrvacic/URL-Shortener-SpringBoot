package codes.karlo.api.converter;

import codes.karlo.api.dto.ApiKeyUpdateDto;
import codes.karlo.api.model.ApiKey;
import codes.karlo.api.exception.ApiKeyDoesntExistException;
import codes.karlo.api.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

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
        return existingApiKey;
    }

}
