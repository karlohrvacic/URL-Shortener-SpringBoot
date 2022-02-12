package codes.karlo.api.service;

import codes.karlo.api.entitiy.Url;
import codes.karlo.api.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Url saveUrl(Url url) {
        return urlRepository.save(url);
    }

    @Override
    public List<Url> fetchUrls() {
        return urlRepository.findAll();
    }

    @Override
    public Url fetchUrlByShortUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl);
    }
}
