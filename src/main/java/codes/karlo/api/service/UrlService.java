package codes.karlo.api.service;

import codes.karlo.api.entity.Url;
import codes.karlo.api.exception.UrlNotFoundException;

import java.util.List;

public interface UrlService {

    Url saveUrl(Url url);

    List<Url> fetchUrls();

    Url fetchUrlByShortUrl(String shortUrl) throws UrlNotFoundException;

    String generateShortUrl(int length);
}
