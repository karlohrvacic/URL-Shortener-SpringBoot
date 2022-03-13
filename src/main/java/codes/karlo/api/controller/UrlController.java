package codes.karlo.api.controller;

import codes.karlo.api.entity.Url;
import codes.karlo.api.exception.*;
import codes.karlo.api.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CommonsLog
@RestController
@RequestMapping("api/v1/url")
@CrossOrigin(origins = "*")
public class UrlController {

    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @Operation(summary = "Send URL for shortening")
    @RequestMapping(method = POST, value = {"/new", "/new/{api_key}"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Returns saved URL",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Long URL not specified",
                    content = @Content)
    })
    public Url saveUrl(@Valid @RequestBody Url url,
                       @PathVariable(required = false, name = "api_key") String api_key)
            throws LongUrlNotSpecifiedException, UrlNotFoundException, ApiKeyDoesntExistException, UserDoesntHaveApiKey {

        return urlService.saveUrl(url, api_key);
    }

    @Operation(summary = "Get full URL from generated short URL")
    @GetMapping("/redirect/{short}")
    public Url fetchUrlByShort(@PathVariable("short") String shortUrl) throws UrlNotFoundException {
        return urlService.fetchUrlByShortUrl(shortUrl);
    }

    @Operation(summary = "Get all URLs made by API key owner")
    @GetMapping("/my-urls/{api_key}")
    public List<Url> fetchUrls(@PathVariable("api_key") String apiKey) throws UserDoesntExistException, ApiKeyDoesntExistException {

        return urlService.fetchUrls(apiKey);
    }

}
