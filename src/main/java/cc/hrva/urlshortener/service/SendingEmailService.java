package cc.hrva.urlshortener.service;

import cc.hrva.urlshortener.model.ResetToken;
import cc.hrva.urlshortener.model.User;

public interface SendingEmailService {

    void sendWelcomeEmail(User user);
    void sendEmailAccountDeactivated(User user);
    public void sendEmailForgotPassword(final User user, final ResetToken resetToken);

}
