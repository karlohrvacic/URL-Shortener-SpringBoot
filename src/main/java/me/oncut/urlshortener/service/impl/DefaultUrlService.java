package me.oncut.urlshortener.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.configuration.properties.AppProperties;
import me.oncut.urlshortener.converter.CreateUrlToUrlConverter;
import me.oncut.urlshortener.converter.UrlToPeekUrlConverter;
import me.oncut.urlshortener.converter.UrlUpdateDtoToUrlConverter;
import me.oncut.urlshortener.dto.CreateUrlDto;
import me.oncut.urlshortener.dto.UrlUpdateDto;
import me.oncut.urlshortener.exception.UrlNotFoundException;
import me.oncut.urlshortener.model.ApiKey;
import me.oncut.urlshortener.model.PeekUrl;
import me.oncut.urlshortener.model.Url;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.repository.UrlRepository;
import me.oncut.urlshortener.service.ApiKeyService;
import me.oncut.urlshortener.service.IPAddressService;
import me.oncut.urlshortener.service.UrlService;
import me.oncut.urlshortener.service.UserService;
import me.oncut.urlshortener.validator.ApiKeyValidator;
import me.oncut.urlshortener.validator.UrlValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;

@Service
@CommonsLog
@RequiredArgsConstructor
public class DefaultUrlService implements UrlService {

    private final UserService userService;
    private final UrlValidator urlValidator;
    private final TaskExecutor taskExecutor;
    private final ApiKeyService apiKeyService;
    private final UrlRepository urlRepository;
    private final AppProperties appProperties;
    private final ApiKeyValidator apiKeyValidator;
    private final IPAddressService ipAddressService;
    private final CreateUrlToUrlConverter createUrlToUrlConverter;
    private final UrlUpdateDtoToUrlConverter urlUpdateDtoToUrlConverter;
    private final UrlToPeekUrlConverter urlToPeekUrlConverter;

    @Override
    public RedirectView redirectResultUrl(final String shortUrl, final String clientIP) {
        final RedirectView redirectView = new RedirectView();

        if (StringUtils.isNotEmpty(shortUrl) && urlRepository.existsUrlByShortUrlAndActiveTrue(shortUrl)) {
            redirectView.setUrl(checkIPUniquenessAndReturnUrl(shortUrl, clientIP).getLongUrl());
        } else {
            redirectView.setUrl(appProperties.getFrontendUrl());
        }
        return redirectView;
    }

    @Override
    @Transactional
    public Url saveUrlRouting(final @Valid CreateUrlDto createUrlDto) {
        final Url url = createUrlToUrlConverter.convert(createUrlDto);

        urlValidator.longUrlInUrl(url);
        urlValidator.checkIfUrlExpirationDateIsInThePast(url);

        if (userService.getUserFromToken() != null) {
            return saveUrlWithApiKey(createUrlDto, null);
        } else {
            return createUrlForAnonymousUser(url);
        }
    }

    @Override
    @Transactional
    public Url saveUrlWithApiKey(final @Valid CreateUrlDto createUrlDto, final String key) {
        final Url url = createUrlToUrlConverter.convert(createUrlDto);
        final ApiKey apiKey = getApiKey(key);
        setShortUrlForLoggedInUser(url, apiKey);

        log.info("Saving URL");
        return urlRepository.save(url);
    }

    @Override
    public List<Url> getAllMyUrls(final String apiKey) {
        final User user;
        if (StringUtils.isNotEmpty(apiKey)) {
            apiKeyValidator.apiKeyExistsByKeyAndIsValid(apiKey);
            user = apiKeyService.fetchApiKeyByKey(apiKey).getOwner();
        } else {
            user = userService.getUserFromToken();
        }

        return urlRepository.findAllByOwner(user).orElse(null);
    }

    @Override
    public List<Url> getAllUrls() {
        return urlRepository.findAll();
    }

    @Override
    public Url revokeUrl(final Long id) {
        final Url url = urlRepository.findById(id).orElseThrow(() -> new UrlNotFoundException("Url doesn't exist"));

        urlValidator.verifyUserAdminOrOwner(url);
        url.setActive(false);
        return urlRepository.save(url);
    }

    @Override
    @Transactional
    public void deleteUrl(final Long id) {
        final Url url = urlRepository.findById(id).orElseThrow(() -> new UrlNotFoundException("Url doesn't exist"));

        urlValidator.verifyUserAdminOrOwner(url);

        ipAddressService.deleteRecordsForUrl(url);
        urlRepository.delete(url);
    }

    @Override
    @Transactional
    public Url updateUrl(final UrlUpdateDto updateDto) {
        final Url url = urlUpdateDtoToUrlConverter.convert(updateDto);
        urlValidator.checkIfUrlExpirationDateIsInThePast(url);

        url.verifyUrlValidity();
        return urlRepository.save(url);
    }

    @Override
    @Transactional
    public Url checkIPUniquenessAndReturnUrl(final String shortUrl, final String clientIP) {
        final Url url = findUrlByShortUrlAndActive(shortUrl);

        asyncCheckIfVisitUnique(clientIP, url);
        return url;
    }

    @Override
    public PeekUrl peekUrlByShortUrl(final String shortUrl) {
        final Url url = findUrlByShortUrlAndActive(shortUrl);

        return urlToPeekUrlConverter.convert(url);
    }

    @Override
    public void deactivateExpiredUrls() {
        final List<Url> urls = urlRepository.findByExpirationDateLessThanEqualAndActiveTrue(LocalDateTime.now()).stream()
                .map(url -> {
                    url.setActive(false);
                    return url;
                })
                .toList();

        urlRepository.saveAll(urls);
        if (!urls.isEmpty()) log.info(String.format("Deactivated %d urls", urls.size()));
    }

    @Override
    public Url getUrlByLongUrl(final String longUrl) {
        return urlRepository.findByLongUrlAndActiveTrue(longUrl).orElseThrow(() -> new UrlNotFoundException("URL doesn't exist"));
    }

    @Override
    public String generateShortUrl(final Long length) {
        return RandomStringUtils.random(Math.toIntExact(length), true, true);
    }

    private Url findUrlByShortUrlAndActive(final String shortUrl) {
        return urlRepository.findByShortUrlAndActiveTrue(shortUrl).orElseThrow(() -> new UrlNotFoundException("URL doesn't exist"));
    }

    private Url createUrlForAnonymousUser(final Url url) {
        url.clearForAnonymousUser();

        if (urlRepository.existsUrlByLongUrlAndActiveTrueAndOwnerIsNull(url.getLongUrl())) {
            final Url existingLongUrl = getUrlByLongUrl(url.getLongUrl());
            log.warn("Long url already exists in DB, will return URL from long URL " + existingLongUrl.getShortUrl());
            return existingLongUrl;
        }

        url.setShortUrl(generateShortUrl(appProperties.getShortUrlLength()));

        urlValidator.checkIfShortUrlIsUnique(url.getShortUrl());

        log.info("Saving URL");
        return urlRepository.save(url);
    }

    private ApiKey getApiKey(String key) {
        final ApiKey apiKey;

        if (StringUtils.isNotEmpty(key)) {
            apiKey = apiKeyService.fetchApiKeyByKey(key);
        } else {
            apiKey = getFirstApiKeyForLoggedInUser();
            key = apiKey.getKey();
        }

        apiKeyValidator.apiKeyExistsByKeyAndIsValid(key);
        return apiKey;
    }

    private void asyncCheckIfVisitUnique(final String clientIP, final Url url) {
        taskExecutor.execute(() -> {
            if (!ipAddressService.urlAlreadyVisitedByIP(url, clientIP)) {
                incrementVisitForUrl(url);
            }
        });
    }

    private void incrementVisitForUrl(final Url url) {
        urlRepository.save(url.onVisit());
    }

    private void setShortUrlForLoggedInUser(final @Valid Url url, final ApiKey apiKey) {
        log.info("Setting short URL");

        if (StringUtils.isEmpty(url.getShortUrl())) {
            url.setShortUrl(generateShortUrl(appProperties.getShortUrlLength()));
            log.info(String.format("URL got generated short URL %s", url.getShortUrl()));
        }

        urlValidator.checkIfShortUrlIsUnique(url.getShortUrl());

        url.setApiKey(apiKey);
        url.setOwner(apiKey.getOwner());
        apiKeyService.apiKeyUseAction(apiKey);

        log.info("URL is created with API key and is keeping custom short url: " + url.getShortUrl());
    }

    private ApiKey getFirstApiKeyForLoggedInUser() {
        log.info("User is authenticated but didn't pass API key");

        return userService.getUserFromToken()
                .getApiKeys()
                .stream()
                .filter(ApiKey::isActive)
                .findFirst()
                .orElseGet(apiKeyService::generateNewApiKey);
    }

}
