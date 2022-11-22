package cc.hrva.urlshortener.service;

import cc.hrva.urlshortener.model.Url;

public interface IPAddressService {

    boolean urlAlreadyVisitedByIP(Url url, String clientIP);

    void saveUrlVisitByIP(Url url, String clientIP);

    void deactivateDeprecatedIps();

    void deleteDeactivatedIps();

    void deleteRecordsForUrl(Url url);
}
