package me.oncut.urlshortener.service;

public interface SendingEmailService {
    public void sendEmailForgotPassword(String emailTo, String token);
}
