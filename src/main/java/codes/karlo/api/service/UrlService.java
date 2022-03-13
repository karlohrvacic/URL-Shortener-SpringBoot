package codes.karlo.api.service;

import codes.karlo.api.entity.Url;
import codes.karlo.api.exception.*;

import java.util.List;

public interface UrlService {

    Url saveUrl(Url url, String api_key) throws LongUrlNotSpecifiedException, UrlNotFoundException, ApiKeyDoesntExistException, UserDoesntHaveApiKey;

    List<Url> fetchUrls(String apiKey) throws UserDoesntExistException, ApiKeyDoesntExistException;

    Url fetchUrlByShortUrl(String shortUrl) throws UrlNotFoundException;

    Url fetchUrlByLongUrl(String longUrl) throws UrlNotFoundException;

    String generateShortUrl(int length);
}
