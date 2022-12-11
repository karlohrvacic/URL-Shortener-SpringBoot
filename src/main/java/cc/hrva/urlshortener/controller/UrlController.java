package cc.hrva.urlshortener.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import cc.hrva.urlshortener.dto.CreateUrlDto;
import cc.hrva.urlshortener.dto.UrlUpdateDto;
import cc.hrva.urlshortener.model.PeekUrl;
import cc.hrva.urlshortener.model.Url;
import cc.hrva.urlshortener.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CommonsLog
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/url")
public class UrlController {

    private final UrlService urlService;
    private final HttpServletRequest request;

    @PostMapping("/new")
    public ResponseEntity<Url> saveUrl(@Valid @RequestBody final CreateUrlDto createUrlDto) {
        return ResponseEntity.ok(urlService.saveUrlRouting(createUrlDto));
    }

    @PostMapping("/new/{apiKey}")
    public ResponseEntity<Url> saveUrlWithApiKey(@Valid @RequestBody final CreateUrlDto createUrlDto,
                                 @PathVariable(required = false, value = "apiKey") final String apiKey) {
        return ResponseEntity.ok(urlService.saveUrlWithApiKey(createUrlDto, apiKey));
    }

    @GetMapping("/redirect/{short}")
    public ResponseEntity<Url> fetchUrlByShort(@PathVariable("short") final String shortUrl) {
        var remoteAddress = "";
        if (request != null) {
            remoteAddress = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddress == null || "".equals(remoteAddress)) {
                remoteAddress = request.getRemoteAddr();
            }
        }

        return ResponseEntity.ok(urlService.checkIPUniquenessAndReturnUrl(shortUrl, remoteAddress));
    }

    @GetMapping("/peek/{short}")
    public ResponseEntity<PeekUrl> peekUrlByShortUrl(@PathVariable("short") final String shortUrl) {
        return ResponseEntity.ok(urlService.peekUrlByShortUrl(shortUrl));
    }

    @PutMapping()
    public ResponseEntity<Url> updateUrl(@Valid @RequestBody final UrlUpdateDto urlUpdateDto) {
        return ResponseEntity.ok(urlService.updateUrl(urlUpdateDto));
    }

    @GetMapping("/my/{apiKey}")
    public ResponseEntity<List<Url>> getAllMyUrlsWithApiKey(@PathVariable("apiKey") final String apiKey) {
        return ResponseEntity.ok(urlService.getAllMyUrls(apiKey));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Url>> getAllMyUrls() {
        return ResponseEntity.ok(urlService.getAllMyUrls(null));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Url>> getAllUrls() {
        return ResponseEntity.ok(urlService.getAllUrls());
    }

    @GetMapping("/deactivate/{id}")
    public ResponseEntity<Url> revokeUrl(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(urlService.revokeUrl(id));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUrl(@PathVariable("id") final Long id) {
        urlService.deleteUrl(id);

        return ResponseEntity.noContent().build();
    }

}
