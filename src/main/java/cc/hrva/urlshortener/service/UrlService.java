package cc.hrva.urlshortener.service;

import java.util.List;
import cc.hrva.urlshortener.dto.CreateUrlDto;
import cc.hrva.urlshortener.dto.UrlUpdateDto;
import cc.hrva.urlshortener.model.PeekUrl;
import cc.hrva.urlshortener.model.Url;
import org.springframework.web.servlet.view.RedirectView;

public interface UrlService {

    List<Url> getAllUrls();
    Url revokeUrl(Long id);
    void deleteUrl(Long id);
    void deactivateExpiredUrls();
    Url updateUrl(UrlUpdateDto url);
    Url getUrlByLongUrl(String longUrl);
    String generateShortUrl(Long length);
    Url saveUrlRouting(CreateUrlDto url);
    List<Url> getAllMyUrls(String apiKey);
    PeekUrl peekUrlByShortUrl(String shortUrl);
    Url saveUrlWithApiKey(CreateUrlDto createUrlDto, String apiKey);
    RedirectView redirectResultUrl(String shortUrl, String clientIP);
    Url checkIPUniquenessAndReturnUrl(String shortUrl, String clientIP);

}
