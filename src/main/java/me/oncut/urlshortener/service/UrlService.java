package me.oncut.urlshortener.service;

import me.oncut.urlshortener.model.Url;
import java.util.List;
import org.springframework.web.servlet.view.RedirectView;

public interface UrlService {

    List<Url> getMyAllUrls(String apiKey);

    Url getUrlByShortUrl(String shortUrl);

    Url getUrlByLongUrl(String longUrl);

    String generateShortUrl(Long length);

    Url saveUrlRouting(Url url);

    Url saveUrlWithApiKey(Url url, String apiKey);

    RedirectView redirectResultUrl(String shortUrl);
}
