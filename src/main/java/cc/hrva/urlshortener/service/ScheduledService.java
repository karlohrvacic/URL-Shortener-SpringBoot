package cc.hrva.urlshortener.service;

public interface ScheduledService {

    void deleteDeactivatedIps();
    void deactivateExpiredUrls();
    void deactivateDeprecatedIps();
    void deactivateExpiredApiKeys();
    void deactivateUnusedUserAccounts();
    void deleteExpiredPasswordResetTokens();
    void deactivateExpiredPasswordResetTokens();

}
