package me.oncut.urlshortener.service;

public interface EmailService {

  public void sendEmail(String toEmail, String subject, String body);

}
