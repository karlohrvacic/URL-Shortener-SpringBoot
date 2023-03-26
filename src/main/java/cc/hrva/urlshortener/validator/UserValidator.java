package cc.hrva.urlshortener.validator;

public interface UserValidator {

    void checkRegistrationEnabled();
    void checkEmailUniqueness(String email);

}
