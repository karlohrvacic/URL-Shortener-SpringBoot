package codes.karlo.api.controller;

import codes.karlo.api.model.Url;
import codes.karlo.api.service.UrlService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
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
    public Url saveUrlRandomShortUrl(@Valid @RequestBody final Url url) {
        return urlService.saveUrlRandomShortUrl(url);
    }

    @PostMapping("/new/{apiKey}")
    public Url saveUrlWithApiKey(@Valid @RequestBody final Url url,
                                 @PathVariable(required = false, value = "apiKey") final String apiKey) {
        return urlService.saveUrlWithApiKey(url, apiKey);

    }

    @GetMapping("/redirect/{short}")
    public Url fetchUrlByShort(@PathVariable("short") final String shortUrl) {
        return urlService.fetchUrlByShortUrl(shortUrl);
    }

    @GetMapping("/my-urls/{apiKey}")
    public List<Url> fetchUrls(@PathVariable("apiKey") final String apiKey) {
        return urlService.fetchUrls(apiKey);
    }

}
