package me.oncut.urlshortener.service.impl;

import lombok.RequiredArgsConstructor;
import me.oncut.urlshortener.config.AppProperties;
import me.oncut.urlshortener.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final AppProperties appProperties;

    @Override
    public void sendEmail(final String toEmail, final String subject, final String body) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(appProperties.getEmailSenderAddress());
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }

}
