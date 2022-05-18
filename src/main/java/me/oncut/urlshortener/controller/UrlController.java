package me.oncut.urlshortener.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.exception.ApiKeyDoesntExistException;
import me.oncut.urlshortener.exception.UserDoesntExistException;
import me.oncut.urlshortener.model.Url;
import me.oncut.urlshortener.service.UrlService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CommonsLog
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/new")
    public Url saveUrl(@Valid @RequestBody final Url url) {
        return urlService.saveUrlRouting(url);
    }

    @PostMapping("/new/{apiKey}")
    public Url saveUrlWithApiKey(@Valid @RequestBody final Url url,
                                 @PathVariable(required = false, value = "apiKey") final String apiKey) {
        return urlService.saveUrlWithApiKey(url, apiKey);

    }

    @GetMapping("/redirect/{short}")
    public Url fetchUrlByShort(@PathVariable("short") final String shortUrl) {
        return urlService.getUrlByShortUrl(shortUrl);
    }

    @GetMapping("/my/{apiKey}")
    public List<Url> getAllMyUrlsWithApiKey(@PathVariable("apiKey") final String apiKey) {
        return urlService.getAllMyUrls(apiKey);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Url> getAllMyUrls() {
        return urlService.getAllMyUrls(null);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Url> getAllUrls() {
        return urlService.getAllUrls();
    }

    @GetMapping("/revoke/{id}")
    public Url revokeApiKey(@PathVariable("id") final Long id) throws UserDoesntExistException, ApiKeyDoesntExistException {
        return urlService.revokeUrl(id);
    }

}
