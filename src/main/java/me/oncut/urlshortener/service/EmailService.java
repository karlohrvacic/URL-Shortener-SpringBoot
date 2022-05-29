package me.oncut.urlshortener.service;

import java.io.File;
import javax.mail.MessagingException;
import me.oncut.urlshortener.model.Email;

public interface EmailService {

  public void sendEmail(final Email email, final File attachment) throws MessagingException;

}
