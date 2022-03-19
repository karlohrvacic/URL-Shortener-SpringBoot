package codes.karlo.api.validator;

import codes.karlo.api.entity.Url;

public interface UrlValidator {

    void longUrlInUrl(Url url);

    void checkIfShortUrlIsUnique(String shortUrl);
}
