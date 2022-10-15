package me.oncut.urlshortener.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.configuration.properties.AppProperties;
import me.oncut.urlshortener.model.IPAddress;
import me.oncut.urlshortener.model.Url;
import me.oncut.urlshortener.repository.IPAddressRepository;
import me.oncut.urlshortener.service.IPAddressService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CommonsLog
@RequiredArgsConstructor
public class DefaultIPAddressService implements IPAddressService {

    private final PasswordEncoder encoder;
    private final AppProperties appProperties;
    private final IPAddressRepository ipAddressRepository;

    @Override
    public boolean urlAlreadyVisitedByIP(final Url url, final String clientIP) {
        final var ipAddresses = ipAddressRepository.findByUrlAndActiveTrue(url);
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
        final var ipAddress = IPAddress.builder()
                .hashedIPAddress(encoder.encode(clientIP))
                .url(url)
                .build();

        ipAddressRepository.save(ipAddress);
    }

    @Override
    public void deactivateDeprecatedIps() {
        final var ipAddresses = ipAddressRepository.findByCreateDateIsLessThanEqualAndActiveTrue(
                LocalDateTime.now().minusHours(appProperties.getInactiveVisitIncrementPerIpInHours())).stream()
                .map(ipAddress -> {
                    ipAddress.setActive(false);
                    log.info(String.format("Deactivated IP address with id %d", ipAddress.getId()));
                    return ipAddress;
                })
                .toList();

        ipAddressRepository.saveAll(ipAddresses);
        if (!ipAddresses.isEmpty()) log.info(String.format("Deactivated %d IP addresses", ipAddresses.size()));
    }

    @Override
    public void deleteDeactivatedIps() {
        final var ipAddresses = ipAddressRepository.findByCreateDateIsLessThanEqualAndActiveFalse(
                LocalDateTime.now().minusHours(appProperties.getIpRetentionDurationInHours())).stream()
                .map(ipAddress -> {
                    ipAddress.setActive(false);
                    return ipAddress;
                })
                .toList();

        ipAddressRepository.deleteAll(ipAddresses);
        if (!ipAddresses.isEmpty()) log.info(String.format("Deleted %d IP addresses", ipAddresses.size()));
    }

    @Override
    @Transactional
    public void deleteRecordsForUrl(final Url url) {
        ipAddressRepository.deleteAll(ipAddressRepository.findAllByUrl(url));
    }

    private void addVisitToUrl(final IPAddress ipAddress) {
        final var savedIpAddress = ipAddressRepository.save(ipAddress.addVisit());
        log.info(String.format("IP address with id %d saved", savedIpAddress.getId()));
    }

}
