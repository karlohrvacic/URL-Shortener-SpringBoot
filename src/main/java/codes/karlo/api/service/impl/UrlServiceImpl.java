package codes.karlo.api.service.impl;

import codes.karlo.api.entity.ApiKey;
import codes.karlo.api.entity.Url;
import codes.karlo.api.entity.User;
import codes.karlo.api.exception.ApiKeyDoesntExistException;
import codes.karlo.api.exception.LongUrlNotSpecifiedException;
import codes.karlo.api.exception.UrlNotFoundException;
import codes.karlo.api.repository.UrlRepository;
import codes.karlo.api.service.ApiKeyService;
import codes.karlo.api.service.UrlService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final ApiKeyService apiKeyService;

    @Value("${url.short-length}")
    private int SHORT_URL_LENGTH;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository, ApiKeyService apiKeyService) {
        this.urlRepository = urlRepository;
        this.apiKeyService = apiKeyService;
    }

    @Override
    public Url saveUrl(Url url, String api_key) throws LongUrlNotSpecifiedException, UrlNotFoundException, ApiKeyDoesntExistException {

        if (url.getLongUrl() == null) throw new LongUrlNotSpecifiedException("URL for shortening is not specified");

        setShortUrl(url, api_key);

        try {
            return urlRepository.save(url);
        } catch (DataIntegrityViolationException e) {
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

    private void setShortUrl(Url url, String api_key) throws ApiKeyDoesntExistException {

        ApiKey apiKey = api_key != null ? apiKeyService.fetchApiKeyByKey(api_key) : null;

        if (url.getShortUrl() == null || apiKey == null) {
            url.setShortUrl(generateShortUrl(SHORT_URL_LENGTH));

        } else {
            url.setApiKey(apiKey);
            url.setOwner(apiKey.getOwner());
            apiKeyService.apiKeyUseAction(apiKey);
        }
    }
}
