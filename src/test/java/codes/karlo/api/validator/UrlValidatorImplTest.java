package codes.karlo.api.validator;

import codes.karlo.api.exception.LongUrlNotSpecifiedException;
import codes.karlo.api.exception.ShortUrlAlreadyExistsException;
import codes.karlo.api.model.Url;
import codes.karlo.api.repository.UrlRepository;
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

    @BeforeEach
    void setUp() {
        this.urlValidator = new UrlValidatorImpl(urlRepository);
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