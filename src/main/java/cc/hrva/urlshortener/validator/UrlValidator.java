package cc.hrva.urlshortener.validator;

import cc.hrva.urlshortener.model.Url;

public interface UrlValidator {
    void longUrlInUrl(Url url);

    void checkIfShortUrlIsUnique(String shortUrl);

    void verifyUserAdminOrOwner(Url url);

    void checkIfUrlExpirationDateIsInThePast(Url url);

    void checkIfUrlSafe(Url url);

    void checkIfAnonymousUrlCreationEnabled();
}
