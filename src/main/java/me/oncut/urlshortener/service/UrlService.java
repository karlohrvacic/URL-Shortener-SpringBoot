package me.oncut.urlshortener.service;

import java.util.List;
import me.oncut.urlshortener.model.Url;
import org.springframework.web.servlet.view.RedirectView;

public interface UrlService {

    List<Url> getAllMyUrls(String apiKey);

    Url getUrlByShortUrl(String shortUrl);

    Url getUrlByLongUrl(String longUrl);

    String generateShortUrl(Long length);

    Url saveUrlRouting(Url url);

    Url saveUrlWithApiKey(Url url, String apiKey);

    RedirectView redirectResultUrl(String shortUrl);

    List<Url> getAllUrls();

    Url revokeUrl(Long id);
}
