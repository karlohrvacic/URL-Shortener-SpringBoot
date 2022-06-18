package me.oncut.urlshortener.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.config.AppProperties;
import me.oncut.urlshortener.model.IPAddress;
import me.oncut.urlshortener.model.Url;
import me.oncut.urlshortener.repository.IPAddressRepository;
import me.oncut.urlshortener.service.IPAddressService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
@RequiredArgsConstructor
public class IPAddressServiceImpl implements IPAddressService {

    private final PasswordEncoder encoder;
    private final IPAddressRepository ipAddressRepository;
    private final AppProperties appProperties;

    @Override
    public boolean urlAlreadyVisitedByIP(final Url url, final String clientIP) {
        final List<IPAddress> ipAddresses = ipAddressRepository.findByUrlAndActiveTrue(url);
        for (final IPAddress ipAddress : ipAddresses) {
            if (encoder.matches(clientIP, ipAddress.getHashedIPAddress())) {
                addVisitToUrl(ipAddress);
                return true;
            }
        }
        saveUrlVisitByIP(url, clientIP);
        return false;
    }

    @Override
    public void saveUrlVisitByIP(final Url url, final String clientIP) {
        final IPAddress ipAddress = IPAddress.builder()
                .hashedIPAddress(encoder.encode(clientIP))
                .url(url)
                .build();

        ipAddressRepository.save(ipAddress);
    }

    @Override
    public void deactivateDeprecatedIps() {
        final List<IPAddress> ipAddresses = ipAddressRepository.findByActiveTrue();

        for (final IPAddress ipAddress : ipAddresses) {
            if (ipAddress.getCreateDate().plusHours(appProperties.getInactiveVisitIncrementPerIpInHours()).isBefore(LocalDateTime.now())) {
                ipAddress.setActive(false);
                ipAddressRepository.save(ipAddress);
                log.info(String.format("IP address with id %d deactivated", ipAddress.getId()));
            }
        }
    }

    @Override
    public void deleteDeactivatedIps() {
        final List<IPAddress> ipAddresses = ipAddressRepository.findByActiveFalse();

        for (final IPAddress ipAddress : ipAddresses) {
            if (ipAddress.getCreateDate().plusHours(appProperties.getIpRetentionDurationInHours()).isBefore(LocalDateTime.now())) {
                ipAddressRepository.delete(ipAddress);
                log.info(String.format("IP address with id %d deleted", ipAddress.getId()));
            }
        }
    }

    private void addVisitToUrl(final IPAddress ipAddress) {
        final IPAddress savedIpAddress = ipAddressRepository.save(ipAddress.addVisit());
        log.info(String.format("IP address with id %d saved", savedIpAddress.getId()));
    }
}
