package me.oncut.urlshortener.service;

import java.util.List;
import javax.validation.Valid;
import me.oncut.urlshortener.dto.CreateUrlDto;
import me.oncut.urlshortener.dto.UrlUpdateDto;
import me.oncut.urlshortener.model.Url;
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

    void deactivateExpiredUrls();

}
