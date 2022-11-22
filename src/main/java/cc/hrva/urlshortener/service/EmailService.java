package cc.hrva.urlshortener.service;

import java.io.File;
import javax.mail.MessagingException;
import cc.hrva.urlshortener.model.Email;

public interface EmailService {

    void sendEmail(final Email email, final File attachment) throws MessagingException;

}
