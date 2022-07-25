package me.oncut.urlshortener.service.impl;

import lombok.RequiredArgsConstructor;
import me.oncut.urlshortener.service.ApiKeyService;
import me.oncut.urlshortener.service.IPAddressService;
import me.oncut.urlshortener.service.ResetTokenService;
import me.oncut.urlshortener.service.ScheduledService;
import me.oncut.urlshortener.service.UrlService;
import me.oncut.urlshortener.service.UserService;
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
