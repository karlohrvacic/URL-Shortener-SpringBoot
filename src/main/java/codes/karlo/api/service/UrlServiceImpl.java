package codes.karlo.api.service;

import codes.karlo.api.entity.ApiKey;
import codes.karlo.api.entity.Url;
import codes.karlo.api.entity.User;
import codes.karlo.api.exception.LongUrlNotSpecifiedException;
import codes.karlo.api.exception.UrlNotFoundException;
import codes.karlo.api.repository.ApiKeyRepository;
import codes.karlo.api.repository.UrlRepository;
import codes.karlo.api.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final UserService userService;
    private final UserRepository userRepository;


    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository, UserService userService, ApiKeyRepository apiKeyRepository, UserRepository userRepository) {
        this.urlRepository = urlRepository;
        this.userService = userService;
        this.apiKeyRepository = apiKeyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Url saveUrl(Url url, String api_key) throws LongUrlNotSpecifiedException, UrlNotFoundException {

        if (url.getLongUrl() == null) throw new LongUrlNotSpecifiedException("URL for shortening is not specified");

        ApiKey apiKey = apiKeyRepository.findApiKeyByApiKey(api_key);

        if (url.getShortUrl() == null || apiKey == null) {
            int SHORT_URL_LENGTH = 10;
            url.setShortUrl(generateShortUrl(SHORT_URL_LENGTH));
        }

        try {
            if (apiKey != null) {
                apiKey.setApiCallsUsed(apiKey.getApiCallsUsed());
                apiKeyRepository.save(apiKey);

                User user = apiKey.getOwner();
                url.setOwner(user);
                user.getUrls().add(url);
                userRepository.save(user);

            } else {
                url = urlRepository.save(url);
            }
            return url;
        } catch (DataIntegrityViolationException e) {
            return fetchUrlByLongUrl(url.getLongUrl());
        }
    }

    @Override
    public List<Url> fetchUrls(String apiKey) {

        User user = apiKeyRepository.findApiKeyByApiKey(apiKey).getOwner();

        return urlRepository.findAllByOwner(user).orElse(null);
    }

    @Override
    public Url fetchUrlByShortUrl(String shortUrl) throws UrlNotFoundException {
        return urlRepository.findByShortUrl(shortUrl)
                .map(url -> {
                    try {
                        return saveUrl(url.onVisit(), null);
                    } catch (LongUrlNotSpecifiedException | UrlNotFoundException e) {
                        e.printStackTrace();
                    }
                    return url;
                })
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
}
