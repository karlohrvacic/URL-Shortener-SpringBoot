package me.oncut.urlshortener.validator;

import me.oncut.urlshortener.model.User;

public interface AuthValidator {

    void passwordMatchesCurrentPassword(User user, String password);
}
