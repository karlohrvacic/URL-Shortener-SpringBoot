package codes.karlo.api.validator;

import codes.karlo.api.model.Url;

public interface UrlValidator {
    void longUrlInUrl(Url url);

    void checkIfShortUrlIsUnique(String shortUrl);
}
