package me.oncut.urlshortener.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import me.oncut.urlshortener.configuration.properties.AppProperties;
import me.oncut.urlshortener.service.LoginAttemptService;
import org.springframework.stereotype.Service;

@Service
public class DefaultLoginAttemptService implements LoginAttemptService {

    private final LoadingCache<String, Integer> attemptsCache;
    private final AppProperties appProperties;

    public DefaultLoginAttemptService(final AppProperties appProperties) {
        super();
        this.appProperties = appProperties;
        attemptsCache = CacheBuilder.newBuilder().
                expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<>() {
                    public Integer load(final String key) {
                        return 0;
                    }
                });
    }

    @Override
    public void loginSucceeded(final String key) {
        attemptsCache.invalidate(key);
    }

    @Override
    public void loginFailed(final String key) {
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (final ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    @Override
    public boolean isBlocked(final String key) {
        try {
            return attemptsCache.get(key) >= appProperties.getMaxLoginAttempts();
        } catch (final ExecutionException e) {
            return false;
        }
    }

}
