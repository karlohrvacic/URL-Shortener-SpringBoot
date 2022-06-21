package me.oncut.urlshortener.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.configuration.properties.AppProperties;
import me.oncut.urlshortener.exception.NoAuthorizationException;
import me.oncut.urlshortener.model.ResetToken;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.repository.ResetTokenRepository;
import me.oncut.urlshortener.service.ResetTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CommonsLog
@RequiredArgsConstructor
public class ResetTokenServiceImpl implements ResetTokenService {

    private final ResetTokenRepository resetTokenRepository;
    private final AppProperties appProperties;

    @Override
    public void deactivateExpiredPasswordResetTokens() {
        deactivateResetTokens(resetTokenRepository.findByExpirationDateIsLessThanEqualAndActiveTrue(LocalDateTime.now()));
    }

    @Override
    public void deactivateActiveResetTokenIfExists(final User user) {
        deactivateResetTokens(resetTokenRepository.findByUserAndActiveTrue(user));
    }

    @Override
    public void deleteExpiredPasswordResetTokens() {
        final List<ResetToken> resetTokens = resetTokenRepository.findByExpirationDateIsLessThanEqualAndActiveFalse(
                LocalDateTime.now().minusHours(appProperties.getIpRetentionDurationInHours()));

        resetTokenRepository.deleteAll(resetTokens);
        if (!resetTokens.isEmpty()) log.info(String.format("Deleted %d reset tokens", resetTokens.size()));
    }

    @Override
    public ResetToken getResetTokenFromUserAndToken(final User user, final String token) {
        return resetTokenRepository.findResetTokenByUserAndTokenAndActiveTrue(user, token)
                .orElseThrow(() -> new NoAuthorizationException("Invalid credentials"));
    }

    @Override
    @Transactional
    public void deactivateAndSaveToken(final ResetToken token) {
        token.setActive(false);
        resetTokenRepository.save(token);
    }

    @Override
    public ResetToken createTokenForUser(final User user) {
        return resetTokenRepository.save(ResetToken.builder()
                .user(user)
                .expirationDate(LocalDateTime.now().plusHours(appProperties.getResetTokenExpirationInHours()))
                .build());
    }

    private void deactivateResetTokens(List<ResetToken> resetTokens) {
        resetTokens = resetTokens.stream()
                .map(resetToken -> {
                    resetToken.setActive(false);
                    return resetToken;
                })
                .toList();

        resetTokenRepository.saveAll(resetTokens);
        if (!resetTokens.isEmpty()) log.info(String.format("Deactivated %d reset tokens", resetTokens.size()));
    }

}
