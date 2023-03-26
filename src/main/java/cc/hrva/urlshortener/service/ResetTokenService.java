package cc.hrva.urlshortener.service;

import cc.hrva.urlshortener.model.ResetToken;
import cc.hrva.urlshortener.model.User;

public interface ResetTokenService {

    void deleteExpiredPasswordResetTokens();
    ResetToken createTokenForUser(User user);
    void deactivateExpiredPasswordResetTokens();
    void deactivateAndSaveToken(ResetToken token);
    void deactivateActiveResetTokenIfExists(User user);
    ResetToken getResetTokenFromUserAndToken(User user, String token);

}
