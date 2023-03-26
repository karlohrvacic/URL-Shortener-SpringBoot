package cc.hrva.urlshortener.validator;

import cc.hrva.urlshortener.model.User;

public interface AuthValidator {

    void passwordMatchesCurrentPassword(User user, String password);

}
