package me.oncut.urlshortener.controller;

import me.oncut.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@CommonsLog
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UrlRedirectController {

    private final UrlService urlService;

    @GetMapping({"/{short}"})
    public RedirectView fetchUrlByShortUrlOrRedirectToFrontend(@PathVariable("short") final String shortUrl) {
        return urlService.redirectResultUrl(shortUrl);
    }

    @GetMapping({"/"})
    public RedirectView rootRedirectToFrontend() {
        return urlService.redirectResultUrl(null);
    }
}
