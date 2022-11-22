package cc.hrva.urlshortener.service;

public interface ScheduledService {

    void deactivateDeprecatedIps();

    void deleteDeactivatedIps();

    void deactivateExpiredPasswordResetTokens();

    void deleteExpiredPasswordResetTokens();

    void deactivateUnusedUserAccounts();

    void deactivateExpiredApiKeys();

    void deactivateExpiredUrls();

}
