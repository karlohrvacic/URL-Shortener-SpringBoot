package me.oncut.urlshortener.service;

public interface ScheduledIPService {

    void deactivateDeprecatedIps();

    void deleteDeactivatedIps();
}
