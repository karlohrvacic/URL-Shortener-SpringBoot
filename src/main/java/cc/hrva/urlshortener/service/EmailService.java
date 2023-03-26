package cc.hrva.urlshortener.service;

import jakarta.mail.MessagingException;
import java.io.File;
import cc.hrva.urlshortener.model.Email;

public interface EmailService {

    void sendEmail(Email email, File attachment) throws MessagingException;

}
