package me.oncut.urlshortener.service.impl;

import lombok.RequiredArgsConstructor;
import me.oncut.urlshortener.service.IPAddressService;
import me.oncut.urlshortener.service.ScheduledService;
import me.oncut.urlshortener.service.UserService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledServiceImpl implements ScheduledService {

    private final IPAddressService ipAddressService;
    private final UserService userService;

    @Override
    @Scheduled(cron = "0 */1 * * * *")
    public void deactivateDeprecatedIps() {
        ipAddressService.deactivateDeprecatedIps();
    }

    @Override
    @Scheduled(cron = "5 */1 * * * *")
    public void deleteDeactivatedIps() {
        ipAddressService.deleteDeactivatedIps();
    }

    @Override
    @Scheduled(cron = "0 */1 * * * *")
    public void deactivateExpiredPasswordResetTokens() {
        userService.deactivateExpiredPasswordResetTokens();
    }

    @Override
    @Scheduled(cron = "5 */1 * * * *")
    public void deleteExpiredPasswordResetTokens() {
        userService.deleteExpiredPasswordResetTokens();
    }

    @Override
    @Scheduled(cron = "0 10 * * 1-5 *")
    public void deactivateUnusedUserAccounts() {
        userService.deactivateUnusedUserAccounts();
    }

}
