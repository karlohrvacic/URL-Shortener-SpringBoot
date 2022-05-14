package me.oncut.urlshortener.validator;

import me.oncut.urlshortener.model.Url;

public interface UrlValidator {
    void longUrlInUrl(Url url);

    void checkIfShortUrlIsUnique(String shortUrl);
}
