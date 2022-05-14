package me.oncut.urlshortener.service.impl;

import me.oncut.urlshortener.config.AppProperties;
import me.oncut.urlshortener.exception.UrlNotFoundException;
import me.oncut.urlshortener.model.ApiKey;
import me.oncut.urlshortener.model.Url;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.repository.UrlRepository;
import me.oncut.urlshortener.service.ApiKeyService;
import me.oncut.urlshortener.service.UrlService;
import me.oncut.urlshortener.service.UserService;
import me.oncut.urlshortener.validator.ApiKeyValidator;
import me.oncut.urlshortener.validator.UrlValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;

@Service
@CommonsLog
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final ApiKeyService apiKeyService;
    private final UserService userService;
    private final UrlValidator urlValidator;
    private final ApiKeyValidator apiKeyValidator;
    private final AppProperties appProperties;

    @Transactional
    @Override
    public Url saveUrlWithApiKey(final Url url, String key) {
        urlValidator.longUrlInUrl(url);
        final ApiKey apiKey;

        if (key != null) {
            apiKey = apiKeyService.fetchApiKeyByKey(key);
        } else {
            apiKey = getFirstApiKeyForLoggedInUser();
            key = apiKey.getKey();
        }

        apiKeyValidator.apiKeyExistsByKeyAndIsValid(key);
        setShortUrlForLoggedInUser(url, apiKey);

        log.info("Saving URL");
        return urlRepository.save(url);
    }

    @Override
    public RedirectView redirectResultUrl(final String shortUrl) {
        final RedirectView redirectView = new RedirectView();
        if (shortUrl != null) {
            try {
                redirectView.setUrl(getUrlByShortUrl(shortUrl).getLongUrl());
            } catch (final UrlNotFoundException ignored) {
                redirectView.setUrl(appProperties.getFrontendUrl());
            }
        } else {
            redirectView.setUrl(appProperties.getFrontendUrl());
        }
        return redirectView;
    }

    @Transactional
    @Override
    public Url saveUrlRouting(final Url url) {
        if (userService.getUserFromToken() != null) {
            return saveUrlWithApiKey(url, null);
        } else {
            return createUrlForAnonymousUser(url);
        }
    }

    private Url createUrlForAnonymousUser(final Url url) {
        url.clearForAnonimousUser();

        urlValidator.longUrlInUrl(url);

        if (urlRepository.existsUrlByLongUrlAndIsActiveTrue(url.getLongUrl())) {
            final Url existingLongUrl = getUrlByLongUrl(url.getLongUrl());
            log.warn("Long url already exists in DB, will return URL from long URL " + existingLongUrl.getShortUrl());
            return existingLongUrl;
        }

        url.setShortUrl(generateShortUrl(appProperties.getShortUrlLength()));

        urlValidator.checkIfShortUrlIsUnique(url.getShortUrl());

        log.info("Saving URL");
        return urlRepository.save(url);
    }

    @Override
    public List<Url> getMyAllUrls(final String apiKey) {
        final User user = apiKeyService.fetchApiKeyByKey(apiKey).getOwner();

        return urlRepository.findAllByOwner(user)
                .orElse(null);
    }

    @Override
    public Url getUrlByShortUrl(final String shortUrl) {
        return urlRepository.findByShortUrlAndIsActiveTrue(shortUrl)
                .map(url -> urlRepository.save(url.onVisit()))
                .orElseThrow(() -> new UrlNotFoundException("URL doesn't exist"));
    }

    @Override
    public Url getUrlByLongUrl(final String longUrl) {
        return urlRepository.findByLongUrlAndIsActiveTrue(longUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL doesn't exist"));
    }

    @Override
    public String generateShortUrl(final Long length) {
        return RandomStringUtils.random(Math.toIntExact(length), true, false);
    }

    private void setShortUrlForLoggedInUser(final Url url, final ApiKey apiKey) {
        log.info("Setting short URL");

        urlValidator.checkIfShortUrlIsUnique(url.getShortUrl());

        if (url.getShortUrl().length() < 1) {
            url.setShortUrl(generateShortUrl(appProperties.getShortUrlLength()));
            log.info(String.format("URL got generated short URL %s", url.getShortUrl()));
        }

        url.setApiKey(apiKey);
        url.setOwner(apiKey.getOwner());
        apiKeyService.apiKeyUseAction(apiKey);

        log.info("URL got api key attached and is keeping custom short url: " + url.getShortUrl());
    }

    private ApiKey getFirstApiKeyForLoggedInUser() {
        log.info("User is authenticated but didn't pass API key");

        return userService.getUserFromToken()
                .getApiKeys()
                .stream()
                .filter(ApiKey::isActive)
                .findFirst()
                .orElse(apiKeyService.generateNewApiKey());
    }
}
