package codes.karlo.api.validator.impl;

import codes.karlo.api.entity.Url;
import codes.karlo.api.exception.LongUrlNotSpecifiedException;
import codes.karlo.api.validator.UrlValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UrlValidatorImpl implements UrlValidator {

    @Override
    public void longUrlInUrl(Url url) {

        if (url.getLongUrl() == null) throw new LongUrlNotSpecifiedException("URL for shortening is not specified");

    }
}

