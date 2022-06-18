package me.oncut.urlshortener.service;

import me.oncut.urlshortener.model.ResetToken;
import me.oncut.urlshortener.model.User;

public interface SendingEmailService {
    public void sendEmailForgotPassword(final User user, final ResetToken resetToken);

    void sendEmailAccountDeactivated(User user);
}
