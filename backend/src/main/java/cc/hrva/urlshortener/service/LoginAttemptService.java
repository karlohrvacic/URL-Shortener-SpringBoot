package cc.hrva.urlshortener.service;

public interface LoginAttemptService {

    void loginFailed(String key);
    boolean isBlocked(String key);
    void loginSucceeded(String key);

}
