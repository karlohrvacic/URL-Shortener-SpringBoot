package me.oncut.urlshortener.validator.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import me.oncut.urlshortener.exception.LongUrlNotSpecifiedException;
import me.oncut.urlshortener.exception.NoAuthorizationException;
import me.oncut.urlshortener.exception.ShortUrlAlreadyExistsException;
import me.oncut.urlshortener.exception.UrlValidationException;
import me.oncut.urlshortener.model.Url;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.repository.UrlRepository;
import me.oncut.urlshortener.service.UserService;
import me.oncut.urlshortener.validator.UrlValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultUrlValidator implements UrlValidator {

    private final UrlRepository urlRepository;

    private final UserService userService;

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
        final User currentUser = userService.getUserFromToken();
        final boolean isCurrentUserAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authorities -> authorities.getName().equals("ROLE_ADMIN"));

        if (!url.getOwner().equals(currentUser) && !isCurrentUserAdmin) {
            throw new NoAuthorizationException("You don't have authorization for this action");
        }
    }

    @Override
    public void checkIfUrlExpirationDateIsInThePast(final Url url) {
        if (url.getExpirationDate() != null &&LocalDateTime.now().isAfter(url.getExpirationDate())) {
            throw new UrlValidationException("Expiration date can't be in the past");
        }
    }
}

