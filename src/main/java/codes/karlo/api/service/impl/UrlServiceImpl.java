package codes.karlo.api.service.impl;

import codes.karlo.api.config.AppProperties;
import codes.karlo.api.entity.ApiKey;
import codes.karlo.api.entity.Url;
import codes.karlo.api.entity.User;
import codes.karlo.api.exception.UrlNotFoundException;
import codes.karlo.api.exception.UserDoesntHaveApiKey;
import codes.karlo.api.repository.UrlRepository;
import codes.karlo.api.service.ApiKeyService;
import codes.karlo.api.service.UrlService;
import codes.karlo.api.service.UserService;
import codes.karlo.api.validator.ApiKeyValidator;
import codes.karlo.api.validator.UrlValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Url saveUrlWithApiKey(final Url url, final String apiKey) {

        urlValidator.longUrlInUrl(url);
        apiKeyValidator.apiKeyExistsByKeyAndIsValid(apiKey);

        setShortUrlForLoggedInUser(url, apiKey);

        log.info("Saving URL");
        return urlRepository.save(url);

    }

    @Transactional
    @Override
    public Url saveUrlRandomShortUrl(final Url url) {
        urlValidator.longUrlInUrl(url);

        if (urlRepository.existsUrlByLongUrl(url.getLongUrl())) {

            final Url existingLongUrl = fetchUrlByLongUrl(url.getLongUrl());
            log.warn("Long url already exists in DB, will return URL from long URL" + existingLongUrl.getShortUrl());
            if (existingLongUrl.isActive()) return existingLongUrl;
        }

        url.setShortUrl(generateShortUrl(appProperties.getUrlShortLength()));

        urlValidator.checkIfShortUrlIsUnique(url.getShortUrl());

        log.info("Saving URL");
        return urlRepository.save(url);
    }

    @Override
    public List<Url> fetchUrls(final String apiKey) {

        final User user = apiKeyService.fetchApiKeyByKey(apiKey).getOwner();

        return urlRepository.findAllByOwner(user)
                .orElse(null);
    }

    @Override
    public Url fetchUrlByShortUrl(final String shortUrl) {

        return urlRepository.findByShortUrl(shortUrl)
                .map(url -> urlRepository.save(url.onVisit()))
                .orElseThrow(() -> new UrlNotFoundException("URL doesn't exist"));
    }

    @Override
    public Url fetchUrlByLongUrl(final String longUrl) {

        return urlRepository.findByLongUrlAndActiveIsTrue(longUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL doesn't exist"));
    }

    @Override
    public String generateShortUrl(final int length) {

        return RandomStringUtils.random(length, true, false);
    }

    private void setShortUrlForLoggedInUser(final Url url, final String key) {

        log.info("Setting short URL");

        ApiKey apiKey = (key != null) ? apiKeyService.fetchApiKeyByKey(key) : null;

        if (apiKey != null) {
            apiKey = apiKeyService.fetchApiKeyByKey(key);
        } else {
            apiKey = getFirstApiKeyForLoggedInUser();
        }

        urlValidator.checkIfShortUrlIsUnique(url.getShortUrl());

        if (url.getShortUrl().length() < 1) {

            url.setShortUrl(generateShortUrl(appProperties.getUrlShortLength()));
            log.info("URL got generated short URL " + url.getShortUrl());

        }

        url.setApiKey(apiKey);
        url.setOwner(apiKey.getOwner());
        apiKeyService.apiKeyUseAction(apiKey);

        log.info("URL got api key attached and is keeping custom short url: " + url.getShortUrl());

    }

    private ApiKey getFirstApiKeyForLoggedInUser() throws UserDoesntHaveApiKey {
        log.info("User is authenticated but didn't pass API key");

        return userService.getUserFromToken().
                getApiKeys()
                .stream()
                .findFirst()
                .orElseThrow(() -> new UserDoesntHaveApiKey("You need to create API key first"));

    }
}
