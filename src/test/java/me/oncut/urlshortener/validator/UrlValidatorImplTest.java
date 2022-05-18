package me.oncut.urlshortener.validator;

import me.oncut.urlshortener.exception.LongUrlNotSpecifiedException;
import me.oncut.urlshortener.exception.ShortUrlAlreadyExistsException;
import me.oncut.urlshortener.model.Url;
import me.oncut.urlshortener.repository.UrlRepository;
import me.oncut.urlshortener.service.UserService;
import me.oncut.urlshortener.validator.impl.UrlValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlValidatorImplTest {

    private UrlValidator urlValidator;

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        this.urlValidator = new UrlValidatorImpl(urlRepository, userService);
    }

    @Test
    void shouldValidateLongUrlInUrl() {
        final Url url = Url.builder().longUrl("LongUrl").build();
        assertThatCode(() -> urlValidator.longUrlInUrl(url))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldFailValidateLongUrlInUrl() {
        final Url url = Url.builder().build();
        assertThatThrownBy(() -> urlValidator.longUrlInUrl(url))
                .isInstanceOf(LongUrlNotSpecifiedException.class)
                .hasMessage("URL for shortening is not specified");
    }

    @Test
    void shouldValidateShortUrlIsUnique() {
        final String shortUrl = "test";
        when(urlRepository.existsUrlByShortUrl(shortUrl)).thenReturn(false);
        assertThatCode(() -> urlValidator.checkIfShortUrlIsUnique(shortUrl))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldFailValidateShortUrlIsUnique() {
        final String shortUrl = "test";
        when(urlRepository.existsUrlByShortUrl(shortUrl)).thenReturn(true);
        assertThatThrownBy(() -> urlValidator.checkIfShortUrlIsUnique(shortUrl))
                .isInstanceOf(ShortUrlAlreadyExistsException.class)
                .hasMessage("Short URL is already in use");
    }
}