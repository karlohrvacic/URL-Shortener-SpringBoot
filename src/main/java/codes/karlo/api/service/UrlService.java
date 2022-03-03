package codes.karlo.api.service;

import codes.karlo.api.entity.Url;
import codes.karlo.api.exception.ApiKeyDoesntExistException;
import codes.karlo.api.exception.LongUrlNotSpecifiedException;
import codes.karlo.api.exception.UrlNotFoundException;
import codes.karlo.api.exception.UserDoesntExistException;

import java.util.List;

public interface UrlService {

    Url saveUrl(Url url, String api_key) throws LongUrlNotSpecifiedException, UrlNotFoundException, ApiKeyDoesntExistException;

    List<Url> fetchUrls(String apiKey) throws UserDoesntExistException;

    Url fetchUrlByShortUrl(String shortUrl) throws UrlNotFoundException;

    Url fetchUrlByLongUrl(String longUrl) throws UrlNotFoundException;

    String generateShortUrl(int length);
}
