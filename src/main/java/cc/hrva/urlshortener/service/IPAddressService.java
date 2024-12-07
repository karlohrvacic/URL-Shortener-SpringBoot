package cc.hrva.urlshortener.service;

import cc.hrva.urlshortener.model.Url;

public interface IPAddressService {

    void deleteDeactivatedIps();
    void deactivateDeprecatedIps();
    void deleteRecordsForUrl(Url url);
    void saveUrlVisitByIP(Url url, String clientIP);
    boolean urlAlreadyVisitedByIP(Url url, String clientIP);

}
