package codes.karlo.api.service;

import codes.karlo.api.entity.Url;

import java.util.List;

public interface UrlService {

    Url saveUrl(Url url);

    List<Url> fetchUrls();

    Url fetchUrlByShortUrl(String shortUrl);
}
