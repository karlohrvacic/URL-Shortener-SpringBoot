package codes.karlo.api.service;

import codes.karlo.api.entity.Url;
import codes.karlo.api.exception.LongUrlNotSpecifiedException;
import codes.karlo.api.exception.UrlNotFoundException;
import codes.karlo.api.repository.UrlRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public Url saveUrl(Url url) throws LongUrlNotSpecifiedException, UrlNotFoundException {
        if (url.getLongUrl() == null) throw new LongUrlNotSpecifiedException("URL for shortening is not specified");

        if (url.getShortUrl() == null) {
            int SHORT_URL_LENGTH = 10;
            url.setShortUrl(generateShortUrl(SHORT_URL_LENGTH));
        }
        Url savedUrl;
        try {
            savedUrl = urlRepository.save(url);
        } catch (DataIntegrityViolationException e) {
            savedUrl = fetchUrlByLongUrl(url.getLongUrl());
        }

        return savedUrl;
    }

    @Override
    public List<Url> fetchUrls() {
        return urlRepository.findAll();
    }

    @Override
    public Url fetchUrlByShortUrl(String shortUrl) throws UrlNotFoundException {
        return urlRepository.findByShortUrl(shortUrl)
                .map(url -> {
                    try {
                        return saveUrl(url.onVisit());
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
