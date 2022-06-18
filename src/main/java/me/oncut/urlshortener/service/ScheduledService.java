package me.oncut.urlshortener.service;

public interface ScheduledService {

    void deactivateDeprecatedIps();

    void deleteDeactivatedIps();

    void deactivateExpiredPasswordResetTokens();

    void deleteExpiredPasswordResetTokens();

}
