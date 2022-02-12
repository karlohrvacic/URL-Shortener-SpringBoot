package codes.karlo.api.controller;

import codes.karlo.api.entity.Url;
import codes.karlo.api.service.UrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/url")
public class UrlController {

    private final UrlService urlService;

    private final Logger logger = LoggerFactory.getLogger(UrlController.class);

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public Url saveUrl(@Valid @RequestBody Url url) {
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
