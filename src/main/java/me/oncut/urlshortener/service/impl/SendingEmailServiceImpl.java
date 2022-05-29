package me.oncut.urlshortener.service.impl;

import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.config.AppProperties;
import me.oncut.urlshortener.model.Email;
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
        final String emailText = String.format(PASSWORD_RESET_CONTENT,
                user.getName(), resetToken.getCreateDate().toLocalTime().toString(), appProperties.getFrontendUrl(),
                resetToken.getToken(), appProperties.getFrontendUrl(), appProperties.getFrontendUrl(), resetToken.getToken(),
                appProperties.getResetTokenExpirationInHours());

        final Email email = Email.builder()
                .sender(appProperties.getEmailSenderAddress())
                .receivers(new String[] {user.getEmail()})
                .subject("Password reset token")
                .text(emailText)
                .build();
        try {
            emailService.sendEmail(email, null);
        } catch (final MessagingException e) {
            log.error("Error while trying to set probation expiration email.", e);
        }
    }
}
