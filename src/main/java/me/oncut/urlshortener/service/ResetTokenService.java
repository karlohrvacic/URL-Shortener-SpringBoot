package me.oncut.urlshortener.service;

import me.oncut.urlshortener.model.ResetToken;
import me.oncut.urlshortener.model.User;

public interface ResetTokenService {

    void deactivateExpiredPasswordResetTokens();

    void deleteExpiredPasswordResetTokens();

    void deactivateActiveResetTokenIfExists(User user);

    ResetToken getResetTokenFromUserAndToken(User user, String token);

    void deactivateAndSaveToken(ResetToken token);

    ResetToken createTokenForUser(User user);
}
