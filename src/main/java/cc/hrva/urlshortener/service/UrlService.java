package cc.hrva.urlshortener.service;

import java.util.List;
import javax.validation.Valid;
import cc.hrva.urlshortener.dto.CreateUrlDto;
import cc.hrva.urlshortener.dto.UrlUpdateDto;
import cc.hrva.urlshortener.model.PeekUrl;
import cc.hrva.urlshortener.model.Url;
import org.springframework.web.servlet.view.RedirectView;

public interface UrlService {

    List<Url> getAllMyUrls(String apiKey);

    Url getUrlByLongUrl(String longUrl);

    String generateShortUrl(Long length);

    Url saveUrlRouting(@Valid CreateUrlDto url);

    Url saveUrlWithApiKey(@Valid CreateUrlDto createUrlDto, String apiKey);

    RedirectView redirectResultUrl(String shortUrl, String clientIP);

    List<Url> getAllUrls();

    Url revokeUrl(Long id);

    void deleteUrl(Long id);

    Url updateUrl(UrlUpdateDto url);

    Url checkIPUniquenessAndReturnUrl(String shortUrl, String clientIP);

  PeekUrl peekUrlByShortUrl(String shortUrl);

  void deactivateExpiredUrls();

}
