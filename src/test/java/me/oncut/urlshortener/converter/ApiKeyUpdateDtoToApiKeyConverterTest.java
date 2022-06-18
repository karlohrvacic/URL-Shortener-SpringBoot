package me.oncut.urlshortener.converter;

import me.oncut.urlshortener.dto.ApiKeyUpdateDto;
import me.oncut.urlshortener.exception.ApiKeyDoesntExistException;
import me.oncut.urlshortener.model.ApiKey;
import me.oncut.urlshortener.repository.ApiKeyRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiKeyUpdateDtoToApiKeyConverterTest {

    private ApiKeyUpdateDtoToApiKeyConverter converter;

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @BeforeEach
    void setUp() {
        this.converter = new ApiKeyUpdateDtoToApiKeyConverter(this.apiKeyRepository);
    }

    @Test
    void shouldConvert() {
        final ApiKeyUpdateDto apiKeyUpdateDto = ApiKeyUpdateDto.builder().id(1L).active(true).build();
        final ApiKey apiKey = ApiKey.builder().id(1L).active(true).build();

        when(apiKeyRepository.findById(anyLong())).thenReturn(Optional.ofNullable(apiKey));

        assertThat(converter.convert(apiKeyUpdateDto)).isEqualTo(apiKey);
    }

    @Test
    void shouldFailConvert() {
        final ApiKeyUpdateDto apiKeyUpdateDto = ApiKeyUpdateDto.builder().id(1L).build();

        when(apiKeyRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatCode(() -> converter.convert(apiKeyUpdateDto))
                .isInstanceOf(ApiKeyDoesntExistException.class)
                .hasMessage(String.format("API key with ID %d has not been found", apiKeyUpdateDto.getId()));
    }
}