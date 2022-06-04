package me.oncut.urlshortener.service;

import me.oncut.urlshortener.model.Url;

public interface IPAddressService {

    boolean urlAlreadyVisitedByIP(Url url, String clientIP);

    void saveUrlVisitByIP(Url url, String clientIP);

    void deactivateDeprecatedIps();

    void deleteDeactivatedIps();
}
