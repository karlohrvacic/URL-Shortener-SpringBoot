package cc.hrva.urlshortener.validator.impl;

import cc.hrva.urlshortener.repository.UrlRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import cc.hrva.urlshortener.exception.LongUrlNotSpecifiedException;
import cc.hrva.urlshortener.exception.NoAuthorizationException;
import cc.hrva.urlshortener.exception.ShortUrlAlreadyExistsException;
import cc.hrva.urlshortener.exception.UrlValidationException;
import cc.hrva.urlshortener.model.Url;
import cc.hrva.urlshortener.service.UserService;
import cc.hrva.urlshortener.validator.UrlValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultUrlValidator implements UrlValidator {

    private final UserService userService;
    private final UrlRepository urlRepository;

    @Override
    public void longUrlInUrl(final Url url) {
        if (url.getLongUrl() == null) throw new LongUrlNotSpecifiedException("URL for shortening is not specified");
    }

    @Override
    public void checkIfShortUrlIsUnique(final String shortUrl) {
        if (urlRepository.existsUrlByShortUrlAndActiveTrue(shortUrl)) throw new ShortUrlAlreadyExistsException("Short URL is already in use");
    }

    @Override
    public void verifyUserAdminOrOwner(final Url url) {
        final var currentUser = userService.getUserFromToken();
        final var isCurrentUserAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authorities -> authorities.getName().equals("ROLE_ADMIN"));

        if (!url.getOwner().equals(currentUser) && !isCurrentUserAdmin) {
            throw new NoAuthorizationException("You don't have authorization for this action");
        }
    }

    @Override
    public void checkIfUrlExpirationDateIsInThePast(final Url url) {
        if (url.getExpirationDate() != null && LocalDateTime.now().isAfter(url.getExpirationDate())) {
            throw new UrlValidationException("Expiration date can't be in the past");
        }
    }

}

