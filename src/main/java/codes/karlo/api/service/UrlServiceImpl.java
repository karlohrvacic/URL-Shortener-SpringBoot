package codes.karlo.api.service;

import codes.karlo.api.entity.Url;
import codes.karlo.api.exception.UrlNotFoundException;
import codes.karlo.api.repository.UrlRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlServiceImpl implements UrlService {

    private final int SHORT_URL_LENGTH = 10;

    private final UrlRepository urlRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public Url saveUrl(Url url) {
        if (url.getShortUrl() == null) {
            url.setShortUrl(generateShortUrl(SHORT_URL_LENGTH));
        }
        return urlRepository.save(url);
    }

    @Override
    public List<Url> fetchUrls() {
        return urlRepository.findAll();
    }

    @Override
    public Url fetchUrlByShortUrl(String shortUrl) throws UrlNotFoundException {
        return urlRepository.findByShortUrl(shortUrl)
                .map(url -> saveUrl(url.onVisit()))
                .orElseThrow(() -> new UrlNotFoundException("URL doesn't exist"));
    }

    @Override
    public String generateShortUrl(int length) {

        return RandomStringUtils.random(length, true, false);
    }
}
