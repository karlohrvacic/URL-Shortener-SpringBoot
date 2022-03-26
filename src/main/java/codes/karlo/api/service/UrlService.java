package codes.karlo.api.service;

import codes.karlo.api.entity.Url;

import java.util.List;

public interface UrlService {

    List<Url> fetchUrls(String apiKey);

    Url fetchUrlByShortUrl(String shortUrl);

    Url fetchUrlByLongUrl(String longUrl);

    String generateShortUrl(int length);

    Url saveUrlRandomShortUrl(Url url);

    Url saveUrlWithApiKey(Url url, String apiKey);
}
