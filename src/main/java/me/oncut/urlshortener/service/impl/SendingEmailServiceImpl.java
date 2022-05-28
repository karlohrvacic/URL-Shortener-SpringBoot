package me.oncut.urlshortener.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.service.SendingEmailService;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
@RequiredArgsConstructor
public class SendingEmailServiceImpl implements SendingEmailService {

    private final EmailServiceImpl emailService;

    @Override
    public void sendEmailForgotPassword(final String emailTo, final String token) {
        emailService.sendEmail(emailTo, "Password reset token", "Your token is " + token);
    }
}
