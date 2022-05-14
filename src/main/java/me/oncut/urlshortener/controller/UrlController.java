package me.oncut.urlshortener.controller;

import me.oncut.urlshortener.model.Url;
import me.oncut.urlshortener.service.UrlService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
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

    @GetMapping({"/my/{apiKey}", "/my"})
    public List<Url> getAllMyUrls(@PathVariable(required = false, name = "apiKey") final String apiKey) {
        return urlService.getAllMyUrls(apiKey);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Url> getAllUrls() {
        return urlService.getAllUrls();
    }

}
