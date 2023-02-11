package cc.hrva.urlshortener.beans;

import cc.hrva.urlshortener.configuration.properties.GoogleApiProperties;
import cc.hrva.urlshortener.model.enums.PlatformType;
import cc.hrva.urlshortener.model.enums.ThreatEntryType;
import cc.hrva.urlshortener.model.enums.ThreatType;
import com.google.api.services.safebrowsing.model.ClientInfo;
import com.google.api.services.safebrowsing.model.FindThreatMatchesRequest;
import com.google.api.services.safebrowsing.model.ThreatEntry;
import com.google.api.services.safebrowsing.model.ThreatInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleSafeBrowsingApi {

    private final GoogleApiProperties googleApiProperties;

    public FindThreatMatchesRequest createFindThreatMatchesRequest(List<String> urls) {
        final var findThreatMatchesRequest = new FindThreatMatchesRequest();
        findThreatMatchesRequest.setClient(getClientInfo());
        findThreatMatchesRequest.setThreatInfo(getThreatInfo(urls));

        return findThreatMatchesRequest;
    }

    private ThreatInfo getThreatInfo(List<String> urls) {
        final var threatInfo = new ThreatInfo();
        final var threatEntries = urls.stream().map(this::getThreatEntry).toList();
        final var threatTypes = Arrays.stream(ThreatType.values()).map(ThreatType::getValue).toList();

        threatInfo.setThreatTypes(threatTypes);
        threatInfo.setPlatformTypes(List.of(PlatformType.ANY_PLATFORM.getValue()));
        threatInfo.setThreatEntryTypes(List.of(ThreatEntryType.URL.getValue()));
        threatInfo.setThreatEntries(threatEntries);

        return threatInfo;
    }

    private ClientInfo getClientInfo() {
        final var clientInfo = new ClientInfo();
        clientInfo.setClientId(googleApiProperties.getClientId());
        clientInfo.setClientVersion(googleApiProperties.getClientVersion());

        return clientInfo;
    }

    private ThreatEntry getThreatEntry(final String url) {
        final var threatEntry = new ThreatEntry();
        threatEntry.set("url", url);

        return threatEntry;
    }

}
