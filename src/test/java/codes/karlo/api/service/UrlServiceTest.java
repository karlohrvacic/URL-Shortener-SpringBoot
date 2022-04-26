package codes.karlo.api.service;

import codes.karlo.api.config.AppProperties;
import codes.karlo.api.model.ApiKey;
import codes.karlo.api.model.Url;
import codes.karlo.api.repository.UrlRepository;
import codes.karlo.api.service.impl.UrlServiceImpl;
import codes.karlo.api.validator.ApiKeyValidator;
import codes.karlo.api.validator.UrlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    private UrlService urlService;

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private ApiKeyService apiKeyService;

    @Mock
    private UserService userService;

    @Mock
    private UrlValidator urlValidator;

    @Mock
    private ApiKeyValidator apiKeyValidator;

    @Mock
    private AppProperties appProperties;

    @BeforeEach
    void setUp() {
        this.urlService = new UrlServiceImpl(urlRepository,
                apiKeyService,
                userService,
                urlValidator,
                apiKeyValidator,
                appProperties
        );
    }

    @Test
    void shouldSaveUrlRandomShortUrl() {
        final Url url = Url.builder().longUrl("long").build();
        final Url existingLongUrl = Url.builder().isActive(false).build();

        when(urlRepository.existsUrlByLongUrlAndIsActiveTrue(url.getLongUrl())).thenReturn(false);
        when(appProperties.getShortUrlLength()).thenReturn(1L);
        when(urlRepository.save(url)).thenReturn(url);

        assertThat(urlService.saveUrlRandomShortUrl(url)).isEqualTo(url);

        verify(urlValidator).checkIfShortUrlIsUnique(url.getShortUrl());
        verify(urlValidator).longUrlInUrl(url);
    }

    @Test
    void shouldSaveUrlWithApiKey() {
        final Url url = Url.builder().shortUrl("").build();
        final String api = "apikey";
        final ApiKey apiKey = ApiKey.builder().build();

        when(urlRepository.save(url)).thenReturn(url);
        when(apiKeyService.fetchApiKeyByKey(api)).thenReturn(apiKey);
        assertThat(urlService.saveUrlWithApiKey(url, api)).isEqualTo(url);

        verify(urlValidator).longUrlInUrl(url);
        verify(apiKeyValidator).apiKeyExistsByKeyAndIsValid(api);
        verify(apiKeyService).apiKeyUseAction(any(ApiKey.class));
    }

}