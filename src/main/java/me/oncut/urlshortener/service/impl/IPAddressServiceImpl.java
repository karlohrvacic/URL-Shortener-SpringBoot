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
                //.hashedIPAddress(encoder.encode(clientIP))
                .hashedIPAddress(clientIP)
                .url(url)
                .build();

        ipAddressRepository.save(ipAddress);
    }

    @Override
    public void deactivateDeprecatedIps() {
        final List<IPAddress> ipAddresses = ipAddressRepository.findByActiveTrue();

        for (final IPAddress ipAddress : ipAddresses) {
            if (ipAddress.getCreateDate().isAfter(LocalDateTime.now().plusHours(appProperties.getInactiveVisitIncrementPerIpInHours()))) {
                ipAddress.setActive(false);
                ipAddressRepository.save(ipAddress);
            }
        }
    }

    @Override
    public void deleteDeactivatedIps() {
        final List<IPAddress> ipAddresses = ipAddressRepository.findByActiveFalse();

        for (final IPAddress ipAddress : ipAddresses) {
            if (ipAddress.getCreateDate().isAfter(LocalDateTime.now().plusHours(appProperties.getIpRetentionDurationInHours()))) {
                ipAddressRepository.delete(ipAddress);
            }
        }
    }

    private void addVisitToUrl(final IPAddress ipAddress) {
        ipAddressRepository.save(ipAddress.addVisit());
    }
}
