package me.oncut.urlshortener.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.model.codebook.Authorities;
import me.oncut.urlshortener.repository.AuthoritiesRepository;
import me.oncut.urlshortener.service.AuthoritiesService;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
@RequiredArgsConstructor
public class DefaultAuthoritiesService implements AuthoritiesService {

    private final AuthoritiesRepository authoritiesRepository;

    private static final String DEFAULT_AUTHORITY_NAME = "ROLE_USER";

    @Override
    public Authorities getDefaultAuthority() {
        return authoritiesRepository.findByName(DEFAULT_AUTHORITY_NAME)
                .orElse(authoritiesRepository.getById(1L));
    }

}
