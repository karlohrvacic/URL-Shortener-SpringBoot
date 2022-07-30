package me.oncut.urlshortener.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.configuration.properties.AppProperties;
import me.oncut.urlshortener.model.Email;
import me.oncut.urlshortener.model.ResetToken;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.service.SendingEmailService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@CommonsLog
@RequiredArgsConstructor
public class DefaultSendingEmailService implements SendingEmailService {

    private final DefaultEmailService emailService;
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;

    @Override
    public void sendEmailForgotPassword(final User user, final ResetToken resetToken) {
        final Context ctx = getContext(user);
        ctx.setVariable("request_date", resetToken.getCreateDate().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_TIME));
        ctx.setVariable("token", resetToken.getToken());
        ctx.setVariable("full_password_reset_link", MessageFormat.format("{0}/#/reset-password?token={1}", appProperties.getFrontendUrl(), resetToken.getToken()));

        final String htmlContent = templateEngine.process("reset_password", ctx);

        final Email email = Email.builder()
                .sender(appProperties.getEmailSenderAddress())
                .receivers(new String[]{user.getEmail()})
                .subject("Password reset token")
                .text(htmlContent)
                .build();

        tryToSendEmail(email);
    }

    @Override
    public void sendEmailAccountDeactivated(final User user) {
        final Context ctx = getContext(user);

        final String htmlContent = templateEngine.process("account_deactivated", ctx);

        final Email email = Email.builder()
                .sender(appProperties.getEmailSenderAddress())
                .receivers(new String[]{user.getEmail()})
                .subject("Account deactivated")
                .text(htmlContent)
                .build();

        tryToSendEmail(email);
    }

    @Override
    public void sendWelcomeEmail(final User user) {
        final Context ctx = getContext(user);

        final String htmlContent = templateEngine.process("welcome", ctx);

        final Email email = Email.builder()
                .sender(appProperties.getEmailSenderAddress())
                .receivers(new String[]{user.getEmail()})
                .subject("Welcome")
                .text(htmlContent)
                .build();

        tryToSendEmail(email);
    }

    private void tryToSendEmail(final Email email) {
        log.info(MessageFormat.format("Sending {0} email to {1}", email.getSubject(), Arrays.toString(email.getReceivers())));
        try {
            emailService.sendEmail(email, null);
        } catch (final MessagingException e) {
            log.error("Error while trying to set probation expiration email.", e);
        }
    }

    private Context getContext(final User user) {
        final Context ctx = new Context();
        ctx.setVariable("name", user.getName());
        ctx.setVariable("number_of_api_keys", user.getApiKeySlots().toString());
        ctx.setVariable("app_name", appProperties.getAppName());
        ctx.setVariable("contact_email", appProperties.getContactEmail());
        ctx.setVariable("login_page", MessageFormat.format("{0}/#/login/", appProperties.getFrontendUrl()));
        ctx.setVariable("api_documentation", MessageFormat.format("{0}/swagger-ui/index.html", appProperties.getServerUrl()));
        ctx.setVariable("days_of_inactivity", appProperties.getDeactivateUserAccountAfterDays().toString());
        ctx.setVariable("contact_email", appProperties.getContactEmail());
        ctx.setVariable("password_reset_link", MessageFormat.format("{0}/#/reset-password", appProperties.getFrontendUrl()));
        ctx.setVariable("token_expiration", appProperties.getResetTokenExpirationInHours().toString());
        ctx.setVariable("github_image", encodeResourceImageToBase64("github.png"));
        ctx.setVariable("password_reset_image", encodeResourceImageToBase64("password_reset.png"));

        return ctx;
    }

    private String encodeResourceImageToBase64(final String imageName) {
        try {
            final File file = ResourceUtils.getFile("classpath:images/" + imageName);
            return Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
        }
        catch (final IOException exception) {
            log.error(exception);
            return "";
        }
    }

}
