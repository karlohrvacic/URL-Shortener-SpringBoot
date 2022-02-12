package codes.karlo.api.controller;

import codes.karlo.api.entitiy.Url;
import codes.karlo.api.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/url")
public class UrlController {

    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public Url saveUrl(@RequestBody Url url) {
        return urlService.saveUrl(url);
    }

    @GetMapping
    public List<Url> fetchUrls() {
        return urlService.fetchUrls();
    }

    @GetMapping("/{short}")
    public Url fetchUrlByShort(@PathVariable("short") String shortUrl) {
        return urlService.fetchUrlByShortUrl(shortUrl);
    }

}
