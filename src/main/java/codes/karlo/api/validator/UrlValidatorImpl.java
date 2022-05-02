package codes.karlo.api.validator;

import codes.karlo.api.exception.LongUrlNotSpecifiedException;
import codes.karlo.api.exception.ShortUrlAlreadyExistsException;
import codes.karlo.api.model.Url;
import codes.karlo.api.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UrlValidatorImpl implements UrlValidator {

    private final UrlRepository urlRepository;

    @Override
    public void longUrlInUrl(final Url url) {
        if (url.getLongUrl() == null)
            throw new LongUrlNotSpecifiedException("URL for shortening is not specified");
    }

    @Override
    public void checkIfShortUrlIsUnique(final String shortUrl) {
        if (urlRepository.existsUrlByShortUrl(shortUrl))
            throw new ShortUrlAlreadyExistsException("Short URL is already in use");
    }
}

