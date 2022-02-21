package codes.karlo.api.controller;

import codes.karlo.api.entity.Url;
import codes.karlo.api.exception.LongUrlNotSpecifiedException;
import codes.karlo.api.exception.UrlNotFoundException;
import codes.karlo.api.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Send URL for shortening")
    @PostMapping("/{api_key}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Returns saved URL",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Long URL not specified",
                    content = @Content)
    })
    public Url saveUrl(@Valid @RequestBody Url url,
                       @PathVariable("api_key") String api_key) throws LongUrlNotSpecifiedException, UrlNotFoundException {
        //TODO apiKey optional, gives permission for custom short url

        return urlService.saveUrl(url);
    }

    @Operation(summary = "Get full URL from generated short URL")
    @GetMapping("/{short}")
    public Url fetchUrlByShort(@PathVariable("short") String shortUrl) throws UrlNotFoundException {
        return urlService.fetchUrlByShortUrl(shortUrl);
    }

    @Operation(summary = "Get all URLs made by API key owner")
    @GetMapping("/{api_key}")
    public List<Url> fetchUrls(@PathVariable("api_key") String apiKey) {
        //TODO require AUTH gives all urls from key owner

        return urlService.fetchUrls();
    }

}
