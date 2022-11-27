package cc.hrva.urlshortener.service;

import jakarta.mail.MessagingException;
import java.io.File;
import cc.hrva.urlshortener.model.Email;

public interface EmailService {

    void sendEmail(final Email email, final File attachment) throws MessagingException;

}
