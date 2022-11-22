package cc.hrva.urlshortener.validator;

public interface UserValidator {
    void checkEmailUniqueness(String email);
}
