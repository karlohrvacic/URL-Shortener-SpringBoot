package cc.hrva.urlshortener.validator;

import cc.hrva.urlshortener.model.Url;

public interface UrlValidator {

    void longUrlInUrl(Url url);
    void checkIfUrlSafe(Url url);
    void verifyUserAdminOrOwner(Url url);
    void checkIfAnonymousUrlCreationEnabled();
    void checkIfShortUrlIsUnique(String shortUrl);
    void checkIfUrlExpirationDateIsInThePast(Url url);

}
