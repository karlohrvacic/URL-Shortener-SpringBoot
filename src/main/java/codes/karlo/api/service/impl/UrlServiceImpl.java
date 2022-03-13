package codes.karlo.api.service.impl;

import codes.karlo.api.entity.ApiKey;
import codes.karlo.api.entity.Url;
import codes.karlo.api.entity.User;
import codes.karlo.api.exception.ApiKeyDoesntExistException;
import codes.karlo.api.exception.LongUrlNotSpecifiedException;
import codes.karlo.api.exception.UrlNotFoundException;
import codes.karlo.api.exception.UserDoesntHaveApiKey;
import codes.karlo.api.repository.UrlRepository;
import codes.karlo.api.service.ApiKeyService;
import codes.karlo.api.service.UrlService;
import codes.karlo.api.service.UserService;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CommonsLog
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final ApiKeyService apiKeyService;
    private final UserService userService;

    @Value("${url.short-length}")
    private int SHORT_URL_LENGTH;

    public UrlServiceImpl(UrlRepository urlRepository, ApiKeyService apiKeyService, UserService userService) {
        this.urlRepository = urlRepository;
        this.apiKeyService = apiKeyService;
        this.userService = userService;
    }

    @Override
    public Url saveUrl(Url url, String api_key) throws LongUrlNotSpecifiedException, UrlNotFoundException, ApiKeyDoesntExistException, UserDoesntHaveApiKey {

        if (url.getLongUrl() == null) throw new LongUrlNotSpecifiedException("URL for shortening is not specified");

        setShortUrl(url, api_key);

        try {
            log.info("Saving URL");
            return urlRepository.save(url);
        } catch (DataIntegrityViolationException e) {
            log.warn("Either short or long url already exists in DB, will return URL from long URL");
            return fetchUrlByLongUrl(url.getLongUrl());
        }
    }

    @Override
    public List<Url> fetchUrls(String apiKey) throws ApiKeyDoesntExistException {

        User user = apiKeyService.fetchApiKeyByKey(apiKey).getOwner();

        return urlRepository.findAllByOwner(user)
                .orElse(null);
    }

    @Override
    public Url fetchUrlByShortUrl(String shortUrl) throws UrlNotFoundException {
        return urlRepository.findByShortUrl(shortUrl)
                .map(url -> urlRepository.save(url.onVisit()))
                .orElseThrow(() -> new UrlNotFoundException("URL doesn't exist"));
    }

    @Override
    public Url fetchUrlByLongUrl(String longUrl) throws UrlNotFoundException {

        return urlRepository.findByLongUrl(longUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL doesn't exist"));
    }

    @Override
    public String generateShortUrl(int length) {

        return RandomStringUtils.random(length, true, false);
    }

    private void setShortUrl(Url url, String api_key) throws ApiKeyDoesntExistException, UserDoesntHaveApiKey {

        log.info("Setting short URL");

        ApiKey apiKey = api_key != null ? apiKeyService.fetchApiKeyByKey(api_key) : null;

        if (apiKey == null && userService.getUserFromToken() != null) {
            apiKey = getFirstApiKeyForLoggedInUser();
        }

        if (url.getShortUrl() == null || apiKey == null) {
            url.setShortUrl(generateShortUrl(SHORT_URL_LENGTH));

            log.info("URL got generated short URL " + url.getShortUrl());

        } else {
            url.setApiKey(apiKey);
            url.setOwner(apiKey.getOwner());
            log.info(apiKey);
            apiKeyService.apiKeyUseAction(apiKey);

            log.info("URL got api key attached and is keeping custom short url: " + url.getShortUrl());

        }
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
