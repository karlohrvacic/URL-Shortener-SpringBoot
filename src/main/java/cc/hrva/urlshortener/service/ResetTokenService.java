package cc.hrva.urlshortener.service;

import cc.hrva.urlshortener.model.ResetToken;
import cc.hrva.urlshortener.model.User;

public interface ResetTokenService {

    void deactivateExpiredPasswordResetTokens();

    void deleteExpiredPasswordResetTokens();

    void deactivateActiveResetTokenIfExists(User user);

    ResetToken getResetTokenFromUserAndToken(User user, String token);

    void deactivateAndSaveToken(ResetToken token);

    ResetToken createTokenForUser(User user);
}
