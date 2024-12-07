package cc.hrva.urlshortener.beans;

import cc.hrva.urlshortener.configuration.properties.GoogleApiProperties;
import cc.hrva.urlshortener.model.enums.PlatformType;
import cc.hrva.urlshortener.model.enums.ThreatEntryType;
import cc.hrva.urlshortener.model.enums.ThreatType;
import com.google.api.services.safebrowsing.v4.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleSafeBrowsingApi {

    private final GoogleApiProperties googleApiProperties;

    public GoogleSecuritySafebrowsingV4FindThreatMatchesRequest createFindThreatMatchesRequest(List<String> urls) {
        final var findThreatMatchesRequest = new GoogleSecuritySafebrowsingV4FindThreatMatchesRequest();
        findThreatMatchesRequest.setClient(getClientInfo());
        findThreatMatchesRequest.setThreatInfo(getThreatInfo(urls));

        return findThreatMatchesRequest;
    }

    private GoogleSecuritySafebrowsingV4ThreatInfo getThreatInfo(List<String> urls) {
        final var threatInfo = new GoogleSecuritySafebrowsingV4ThreatInfo();
        final var threatEntries = urls.stream().map(this::getThreatEntry).toList();
        final var threatTypes = Arrays.stream(ThreatType.values()).map(ThreatType::getValue).toList();

        threatInfo.setThreatTypes(threatTypes);
        threatInfo.setPlatformTypes(List.of(PlatformType.ANY_PLATFORM.getValue()));
        threatInfo.setThreatEntryTypes(List.of(ThreatEntryType.URL.getValue()));
        threatInfo.setThreatEntries(threatEntries);

        return threatInfo;
    }

    private GoogleSecuritySafebrowsingV4ClientInfo getClientInfo() {
        final var clientInfo = new GoogleSecuritySafebrowsingV4ClientInfo();
        clientInfo.setClientId(googleApiProperties.getClientId());
        clientInfo.setClientVersion(googleApiProperties.getClientVersion());

        return clientInfo;
    }

    private GoogleSecuritySafebrowsingV4ThreatEntry getThreatEntry(final String url) {
        final var threatEntry = new GoogleSecuritySafebrowsingV4ThreatEntry();
        threatEntry.set("url", url);

        return threatEntry;
    }

}
