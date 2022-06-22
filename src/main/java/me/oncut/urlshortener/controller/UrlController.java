package me.oncut.urlshortener.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.dto.UrlUpdateDto;
import me.oncut.urlshortener.model.Url;
import me.oncut.urlshortener.service.UrlService;
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
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return urlService.checkIPUniquenessAndReturnUrl(shortUrl, remoteAddr);
    }

    @PutMapping()
    public Url updateUrl(@Valid @RequestBody final UrlUpdateDto urlUpdateDto) {
        return urlService.updateUrl(urlUpdateDto);
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

    @GetMapping("/deactivate/{id}")
    public Url revokeUrl(@PathVariable("id") final Long id) {
        return urlService.revokeUrl(id);
    }

    @GetMapping("/delete/{id}")
    public void deleteUrl(@PathVariable("id") final Long id) {
        urlService.deleteUrl(id);
    }

}
