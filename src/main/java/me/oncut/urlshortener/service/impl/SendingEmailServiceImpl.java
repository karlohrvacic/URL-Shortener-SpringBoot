package me.oncut.urlshortener.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.config.AppProperties;
import me.oncut.urlshortener.model.ResetToken;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.service.SendingEmailService;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
@RequiredArgsConstructor
public class SendingEmailServiceImpl implements SendingEmailService {

    private final EmailServiceImpl emailService;

    private final AppProperties appProperties;

    private String PASSWORD_RESET_CONTENT = """
            <h3>Hello %s</h3>
            <p>You requested password reset at %s</p>
            <p>You can reset your password by clicking <a href="%s/#/reset-password?token=%s">here</a></p>
            <p>If link doesn't work visit <a href="%s/#/reset-password">%s/#/reset-password</a> your token is %s </p>
            <p>Token will work for %d hours, if you didn't request change please ignore this email!</p>""";

    @Override
    public void sendEmailForgotPassword(final User user, final ResetToken resetToken) {
        emailService.sendEmail(user.getEmail(), "Password reset token", String.format(PASSWORD_RESET_CONTENT,
                user.getName(), resetToken.getCreateDate().toLocalTime().toString(), appProperties.getFrontendUrl(),
                resetToken.getToken(), appProperties.getFrontendUrl(), appProperties.getFrontendUrl(), resetToken.getToken(),
                appProperties.getResetTokenExpirationInHours()));
    }
}
