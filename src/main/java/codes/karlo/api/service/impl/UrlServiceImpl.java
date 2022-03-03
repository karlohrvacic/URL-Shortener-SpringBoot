package codes.karlo.api.service.impl;

import codes.karlo.api.entity.ApiKey;
import codes.karlo.api.entity.Url;
import codes.karlo.api.entity.User;
import codes.karlo.api.exception.ApiKeyDoesntExistException;
import codes.karlo.api.exception.LongUrlNotSpecifiedException;
import codes.karlo.api.exception.UrlNotFoundException;
import codes.karlo.api.exception.UserDoesntExistException;
import codes.karlo.api.repository.ApiKeyRepository;
import codes.karlo.api.repository.UrlRepository;
import codes.karlo.api.repository.UserRepository;
import codes.karlo.api.service.UrlService;
import codes.karlo.api.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${url.short-length}")
    private int SHORT_URL_LENGTH;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository, UserService userService, ApiKeyRepository apiKeyRepository, UserRepository userRepository) {
        this.urlRepository = urlRepository;
        this.userService = userService;
        this.apiKeyRepository = apiKeyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Url saveUrl(Url url, String api_key) throws LongUrlNotSpecifiedException, UrlNotFoundException, ApiKeyDoesntExistException {

        if (url.getLongUrl() == null) throw new LongUrlNotSpecifiedException("URL for shortening is not specified");

        setShortUrl(url, api_key);

        try {
//            if (apiKey != null) {
//                apiKey.setApiCallsUsed(apiKey.getApiCallsUsed() + 1);
//                apiKeyRepository.save(apiKey);
//
//                User user = apiKey.getOwner();
//                url.setOwner(user);
//                user.getUrls().add(url);
//                userRepository.save(user);
//
//                Url finalUrl = url;
//                url = user.getUrls().stream().filter(u -> u.getLongUrl().equals(finalUrl.getLongUrl())).findFirst()
//                        .orElse(finalUrl);
//            } else {
//                url = urlRepository.save(url);
//            }
            return urlRepository.save(url);
        } catch (DataIntegrityViolationException e) {
            return fetchUrlByLongUrl(url.getLongUrl());
        }
    }

    @Override
    public List<Url> fetchUrls(String apiKey) throws UserDoesntExistException {

        User user = apiKeyRepository.findApiKeyByApiKey(apiKey)
                .map(ApiKey::getOwner)
                .orElseThrow(() -> new UserDoesntExistException("API key doesn't have owner"));

        return urlRepository.findAllByOwner(user).orElse(null);
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
        ApiKey apiKey = apiKeyRepository.findApiKeyByApiKey(api_key)
                .orElseThrow(() -> new ApiKeyDoesntExistException("Sent API key doesn't exist"));

        if (url.getShortUrl() == null || apiKey == null) {

            url.setShortUrl(generateShortUrl(SHORT_URL_LENGTH));
        } else {

            apiKey.setApiCallsUsed(apiKey.getApiCallsUsed() + 1);
            User user = apiKey.getOwner();
            url.setOwner(user);
            user.getUrls().add(url);

            userRepository.save(user);

        }

    }
}
