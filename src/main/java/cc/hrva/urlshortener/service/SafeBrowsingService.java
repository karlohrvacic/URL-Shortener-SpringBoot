package cc.hrva.urlshortener.service;

import java.util.List;

public interface SafeBrowsingService {

    List<String> checkUrlsForThreats(String url);
    List<String> checkUrlsForThreats(List<String> url);

}
