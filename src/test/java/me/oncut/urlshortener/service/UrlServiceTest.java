package me.oncut.urlshortener.service;

import me.oncut.urlshortener.config.AppProperties;
import me.oncut.urlshortener.converter.UrlUpdateDtoToUrlConverter;
import me.oncut.urlshortener.model.ApiKey;
import me.oncut.urlshortener.model.Url;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.repository.UrlRepository;
import me.oncut.urlshortener.service.impl.UrlServiceImpl;
import me.oncut.urlshortener.validator.ApiKeyValidator;
import me.oncut.urlshortener.validator.UrlValidator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    @Mock
    private UrlUpdateDtoToUrlConverter urlUpdateDtoToUrlConverter;

    @BeforeEach
    void setUp() {
        this.urlService = new UrlServiceImpl(urlRepository,
                apiKeyService,
                userService,
                urlValidator,
                apiKeyValidator,
                appProperties,
                urlUpdateDtoToUrlConverter
        );
    }

    @Test
    void shouldSaveUrlRandomShortUrl() {
        final Url url = Url.builder().longUrl("long").build();

        when(urlRepository.existsUrlByLongUrlAndActiveTrue(url.getLongUrl())).thenReturn(false);
        when(appProperties.getShortUrlLength()).thenReturn(1L);
        when(urlRepository.save(url)).thenReturn(url);

        assertThat(urlService.saveUrlRouting(url)).isEqualTo(url);

        verify(urlValidator).checkIfShortUrlIsUnique(url.getShortUrl());
        verify(urlValidator).longUrlInUrl(url);
    }

    @Test
    void shouldReturnSavedUrlRandomShortUrl() {
        final Url url = Url.builder().longUrl("long").build();
        final Url existingLongUrl = Url.builder().longUrl("long").active(true).build();

        when(urlRepository.existsUrlByLongUrlAndActiveTrue(url.getLongUrl())).thenReturn(true);
        when(urlRepository.findByLongUrlAndActiveTrue(url.getLongUrl())).thenReturn(Optional.ofNullable(existingLongUrl));

        assertThat(urlService.saveUrlRouting(url)).isEqualTo(existingLongUrl);
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

    @Test
    void shouldFetchUrls() {
        final String apiKey = "apikey";
        final User user = User.builder().build();
        final ApiKey key = ApiKey.builder().owner(user).build();
        final List<Url> urls = Collections.singletonList(Url.builder().build());

        when(apiKeyService.fetchApiKeyByKey(apiKey)).thenReturn(key);
        when(urlRepository.findAllByOwner(user)).thenReturn(Optional.of(urls));

        assertThat(urlService.getAllMyUrls(apiKey)).isEqualTo(urls);
    }

    @Test
    void shouldFetchUrlByShortUrl() {
        final String shortUrl = "short";
        final Url url = Url.builder().shortUrl(shortUrl).visits(0L).build();

        when(urlRepository.findByShortUrlAndActiveTrue(shortUrl)).thenReturn(Optional.ofNullable(url));
        when(urlRepository.save(url)).thenReturn(url);

        assertThat(urlService.getUrlByShortUrl(shortUrl)).isEqualTo(url);
    }

    @Test
    void shouldsaveUrlWithApiKeyWithFirstApiKeyForLoggedInUser() {
        final Url url = Url.builder().shortUrl("").build();
        final String api = null;
        final ApiKey apiKey = ApiKey.builder().id(1L).key("key").apiCallsUsed(0L).apiCallsLimit(10L).active(true).build();
        final User user = User.builder().id(1L).apiKeys(Collections.singletonList(apiKey)).build();

        when(userService.getUserFromToken()).thenReturn(user);
        when(urlRepository.save(url)).thenReturn(url);

        assertThat(urlService.saveUrlWithApiKey(url, api)).isEqualTo(url);

        verify(urlValidator).longUrlInUrl(url);
        verify(apiKeyValidator).apiKeyExistsByKeyAndIsValid(api);
        verify(apiKeyService).apiKeyUseAction(any(ApiKey.class));
    }

}