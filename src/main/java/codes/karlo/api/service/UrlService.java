package codes.karlo.api.service;

import codes.karlo.api.entity.Url;
import codes.karlo.api.exception.LongUrlNotSpecifiedException;
import codes.karlo.api.exception.UrlNotFoundException;

import java.util.List;

public interface UrlService {

    Url saveUrl(Url url) throws LongUrlNotSpecifiedException, UrlNotFoundException;

    List<Url> fetchUrls();

    Url fetchUrlByShortUrl(String shortUrl) throws UrlNotFoundException;

    Url fetchUrlByLongUrl(String longUrl) throws UrlNotFoundException;

    String generateShortUrl(int length);
}
