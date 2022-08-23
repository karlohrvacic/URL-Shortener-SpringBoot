package me.oncut.urlshortener.service;

import java.util.Collections;
import java.util.Optional;
import me.oncut.urlshortener.configuration.properties.AppProperties;
import me.oncut.urlshortener.converter.CreateUrlToUrlConverter;
import me.oncut.urlshortener.converter.UrlUpdateDtoToUrlConverter;
import me.oncut.urlshortener.dto.CreateUrlDto;
import me.oncut.urlshortener.model.ApiKey;
import me.oncut.urlshortener.model.Url;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.repository.UrlRepository;
import me.oncut.urlshortener.service.impl.DefaultUrlService;
import me.oncut.urlshortener.validator.ApiKeyValidator;
import me.oncut.urlshortener.validator.UrlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.task.TaskExecutor;

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
    private IPAddressService ipAddressService;

    @Mock
    private UrlUpdateDtoToUrlConverter urlUpdateDtoToUrlConverter;

    @Mock
    private TaskExecutor taskExecutor;

    @Mock
    private CreateUrlToUrlConverter createUrlToUrlConverter;

    @BeforeEach
    void setUp() {
        this.urlService = new DefaultUrlService(
            userService,
            urlValidator,
            taskExecutor,
            apiKeyService,
            urlRepository,
            appProperties,
            apiKeyValidator,
            ipAddressService,
            createUrlToUrlConverter,
            urlUpdateDtoToUrlConverter
        );
    }

    @Test
    void shouldSaveUrlRandomShortUrl() {
        final var createUrlDto = CreateUrlDto.builder().longUrl("long").build();
        final var url = Url.builder().longUrl("long").build();

        when(appProperties.getShortUrlLength()).thenReturn(1L);
        when(urlRepository.save(url)).thenReturn(url);
        when(createUrlToUrlConverter.convert(createUrlDto)).thenReturn(url);

        assertThat(urlService.saveUrlRouting(createUrlDto)).isEqualTo(url);

        verify(urlValidator).checkIfShortUrlIsUnique(url.getShortUrl());
        verify(urlValidator).longUrlInUrl(url);
    }

    void shouldReturnSavedUrlRandomShortUrl() {
        final var url = CreateUrlDto.builder().longUrl("long").build();
        final var existingLongUrl = Url.builder().longUrl("long").active(true).build();

        when(urlRepository.existsUrlByLongUrlAndActiveTrue(url.getLongUrl())).thenReturn(true);
        when(urlRepository.findByLongUrlAndActiveTrue(url.getLongUrl())).thenReturn(Optional.ofNullable(existingLongUrl));

        assertThat(urlService.saveUrlRouting(url)).isEqualTo(existingLongUrl);
    }

    @Test
    void shouldSaveUrlWithApiKey() {
        final var createUrlDto = CreateUrlDto.builder().shortUrl("").build();
        final var url = Url.builder().shortUrl("").build();
        final var api = "apikey";
        final var apiKey = ApiKey.builder().build();

        when(urlRepository.save(url)).thenReturn(url);
        when(apiKeyService.fetchApiKeyByKey(api)).thenReturn(apiKey);
        when(createUrlToUrlConverter.convert(createUrlDto)).thenReturn(url);

        assertThat(urlService.saveUrlWithApiKey(createUrlDto, api)).isEqualTo(url);

        verify(apiKeyValidator).apiKeyExistsByKeyAndIsValid(api);
        verify(apiKeyService).apiKeyUseAction(any(ApiKey.class));
    }

    @Test
    void shouldFetchUrls() {
        final var apiKey = "apikey";
        final var user = User.builder().build();
        final var key = ApiKey.builder().owner(user).build();
        final var urls = Collections.singletonList(Url.builder().build());

        when(apiKeyService.fetchApiKeyByKey(apiKey)).thenReturn(key);
        when(urlRepository.findAllByOwner(user)).thenReturn(Optional.of(urls));

        assertThat(urlService.getAllMyUrls(apiKey)).isEqualTo(urls);
    }

    void shouldSaveUrlWithApiKeyWithFirstApiKeyForLoggedInUser() {
        final var createUrlDto = CreateUrlDto.builder().shortUrl("").build();
        final var url = Url.builder().shortUrl("").build();
        final var apiKey = ApiKey.builder().id(1L).key("key").apiCallsUsed(0L).apiCallsLimit(10L).active(true).build();
        final var user = User.builder().id(1L).apiKeys(Collections.singletonList(apiKey)).build();

        when(userService.getUserFromToken()).thenReturn(user);
        when(urlRepository.save(url)).thenReturn(url);

        assertThat(urlService.saveUrlWithApiKey(createUrlDto, null)).isEqualTo(url);

        verify(urlValidator).longUrlInUrl(url);
        verify(apiKeyValidator).apiKeyExistsByKeyAndIsValid(null);
        verify(apiKeyService).apiKeyUseAction(any(ApiKey.class));
    }

}
