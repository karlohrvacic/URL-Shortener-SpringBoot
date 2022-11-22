package cc.hrva.urlshortener.service.impl;

import lombok.RequiredArgsConstructor;
import cc.hrva.urlshortener.service.ApiKeyService;
import cc.hrva.urlshortener.service.IPAddressService;
import cc.hrva.urlshortener.service.ResetTokenService;
import cc.hrva.urlshortener.service.ScheduledService;
import cc.hrva.urlshortener.service.UrlService;
import cc.hrva.urlshortener.service.UserService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class DefaultScheduledService implements ScheduledService {

    private final IPAddressService ipAddressService;
    private final UserService userService;
    private final ApiKeyService apiKeyService;
    private final ResetTokenService resetTokenService;
    private final UrlService urlService;

    @Override
    @Scheduled(cron = "0 */1 * * * *")
    public void deactivateDeprecatedIps() {
        ipAddressService.deactivateDeprecatedIps();
    }

    @Override
    @Scheduled(cron = "0 */1 * * * *")
    public void deleteDeactivatedIps() {
        ipAddressService.deleteDeactivatedIps();
    }

    @Override
    @Scheduled(cron = "0 */1 * * * *")
    public void deactivateExpiredPasswordResetTokens() {
        resetTokenService.deactivateExpiredPasswordResetTokens();
    }

    @Override
    @Scheduled(cron = "0 */1 * * * *")
    public void deleteExpiredPasswordResetTokens() {
        resetTokenService.deleteExpiredPasswordResetTokens();
    }

    @Override
    @Scheduled(cron = "0 0 10 * * *")
    public void deactivateUnusedUserAccounts() {
        userService.deactivateUnusedUserAccounts();
    }

    @Override
    @Scheduled(cron = "0 0 * * * *")
    public void deactivateExpiredApiKeys() {
        apiKeyService.deactivateExpired();
    }

    @Override
    @Scheduled(cron = "0 */1 * * * *")
    public void deactivateExpiredUrls() {
        urlService.deactivateExpiredUrls();
    }

}
