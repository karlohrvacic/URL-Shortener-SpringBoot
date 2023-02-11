package cc.hrva.urlshortener.service;

import java.util.List;

public interface SafeBrowsingService {

    List<String> checkUrlForThreats(List<String> url);
    List<String> checkUrlForThreats(String url);

}
