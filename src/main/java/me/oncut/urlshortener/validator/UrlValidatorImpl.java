package me.oncut.urlshortener.validator;

import me.oncut.urlshortener.exception.LongUrlNotSpecifiedException;
import me.oncut.urlshortener.exception.ShortUrlAlreadyExistsException;
import me.oncut.urlshortener.model.Url;
import me.oncut.urlshortener.repository.UrlRepository;
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

