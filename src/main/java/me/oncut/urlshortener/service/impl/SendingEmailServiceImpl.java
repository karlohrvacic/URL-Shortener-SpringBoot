package me.oncut.urlshortener.service.impl;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.config.AppProperties;
import me.oncut.urlshortener.model.Email;
import me.oncut.urlshortener.model.ResetToken;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.service.SendingEmailService;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@CommonsLog
@RequiredArgsConstructor
public class SendingEmailServiceImpl implements SendingEmailService {

    private final EmailServiceImpl emailService;
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;

    @Override
    public void sendEmailForgotPassword(final User user, final ResetToken resetToken) {
        final Context ctx = new Context();
        ctx.setVariable("name", user.getName());
        ctx.setVariable("request_date", resetToken.getCreateDate().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_TIME));
        ctx.setVariable("password_reset_link", appProperties.getFrontendUrl() + "/#/reset-password/");
        ctx.setVariable("full_password_reset_link", appProperties.getFrontendUrl() + "/#/reset-password/" + resetToken.getToken());
        ctx.setVariable("token_expiration", appProperties.getResetTokenExpirationInHours().toString());
        ctx.setVariable("token", resetToken.getToken());

        final String htmlContent = templateEngine.process("reset_password", ctx);

        final Email email = Email.builder()
                .sender(appProperties.getEmailSenderAddress())
                .receivers(new String[] {user.getEmail()})
                .subject("Password reset token")
                .text(htmlContent)
                .build();
        try {
            emailService.sendEmail(email, null);
        } catch (final MessagingException e) {
            log.error("Error while trying to set probation expiration email.", e);
        }
    }

    @Override
    public void sendEmailAccountDeactivated(final User user, final Long deactivateUserAccountAfterDays) {

    }
}
