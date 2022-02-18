package codes.karlo.api.controller;

import codes.karlo.api.entity.Url;
import codes.karlo.api.exception.LongUrlNotSpecifiedException;
import codes.karlo.api.exception.UrlNotFoundException;
import codes.karlo.api.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/url")
public class UrlController {

    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/{api_key}")
    public Url saveUrl(@Valid @RequestBody Url url,
                       @PathVariable("api_key") String api_key) throws LongUrlNotSpecifiedException, UrlNotFoundException {
        //TODO apiKey optional, gives permission for custom short url

        return urlService.saveUrl(url);
    }

    @GetMapping("/{short}")
    public Url fetchUrlByShort(@PathVariable("short") String shortUrl) throws UrlNotFoundException {
        return urlService.fetchUrlByShortUrl(shortUrl);
    }

    @GetMapping("/{api_key}")
    public List<Url> fetchUrls(@PathVariable("api_key") String apiKey) {
        //TODO require AUTH gives all urls from key owner

        return urlService.fetchUrls();
    }

}
