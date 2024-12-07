package cc.hrva.urlshortener.service.impl;

import cc.hrva.urlshortener.beans.GoogleSafeBrowsingApi;
import cc.hrva.urlshortener.configuration.properties.GoogleApiProperties;
import cc.hrva.urlshortener.service.SafeBrowsingService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.safebrowsing.v4.Safebrowsing;
import com.google.api.services.safebrowsing.v4.model.GoogleSecuritySafebrowsingV4ThreatEntry;
import com.google.api.services.safebrowsing.v4.model.GoogleSecuritySafebrowsingV4ThreatMatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@CommonsLog
@RequiredArgsConstructor
public class DefaultSafeBrowsingService implements SafeBrowsingService {

    private final GoogleApiProperties googleApiProperties;
    private final GoogleSafeBrowsingApi googleSafeBrowsingApi;

    @Override
    public List<String> checkUrlsForThreats(final List<String> urls) {
        List<GoogleSecuritySafebrowsingV4ThreatMatch> threatMatches = new LinkedList<>();

        try {
             threatMatches = getThreatMatches(urls);
        } catch (final IOException exception) {
            log.error("Safe Browsing error", exception);
        } catch (final GeneralSecurityException exception) {
            log.error("Safe Browsing error", exception);
            throw new RuntimeException(exception);
        }

        if (threatMatches != null) {
            return threatMatches.stream()
                    .map(GoogleSecuritySafebrowsingV4ThreatMatch::getThreat)
                    .map(GoogleSecuritySafebrowsingV4ThreatEntry::getUrl)
                    .toList();
        }

        return new ArrayList<>();
    }

    @Override
    public List<String> checkUrlsForThreats(final String url) {
        return checkUrlsForThreats(List.of(url));
    }

    private List<GoogleSecuritySafebrowsingV4ThreatMatch> getThreatMatches(final List<String> urls) throws IOException, GeneralSecurityException {
        final var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final var jsonFactory = GsonFactory.getDefaultInstance();
        final var findThreatMatchesRequest = googleSafeBrowsingApi.createFindThreatMatchesRequest(urls);

        final var safebrowsing = new Safebrowsing.Builder(httpTransport, jsonFactory, null)
                .setApplicationName(googleApiProperties.getClientId())
                .build();

        final var findThreatMatchesResponse = safebrowsing
                .threatMatches()
                .find(findThreatMatchesRequest)
                .setKey(googleApiProperties.getKey())
                .execute();

        return findThreatMatchesResponse.getMatches();
    }

}
