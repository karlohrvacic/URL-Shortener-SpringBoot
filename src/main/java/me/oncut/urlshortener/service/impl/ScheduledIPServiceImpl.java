package me.oncut.urlshortener.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.service.IPAddressService;
import me.oncut.urlshortener.service.ScheduledIPService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledIPServiceImpl implements ScheduledIPService {

    private final IPAddressService ipAddressService;

    @Override
    @Scheduled(cron = "0 */1 * * * *")
    public void deactivateDeprecatedIps() {
        log.info("Cron triggered for deactivation of deprecated IPs");
        ipAddressService.deactivateDeprecatedIps();
    }

    @Override
    @Scheduled(cron = "5 */1 * * * *")
    public void deleteDeactivatedIps() {
        log.info("Cron triggered for deletion of deactivated IPs");
        ipAddressService.deleteDeactivatedIps();
    }
}
