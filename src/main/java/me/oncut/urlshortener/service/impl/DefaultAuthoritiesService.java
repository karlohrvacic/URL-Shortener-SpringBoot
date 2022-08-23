package me.oncut.urlshortener.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.model.codebook.Authorities;
import me.oncut.urlshortener.repository.AuthoritiesRepository;
import me.oncut.urlshortener.service.AuthoritiesService;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
@CommonsLog
@RequiredArgsConstructor
public class DefaultAuthoritiesService implements AuthoritiesService {

    private static final String DEFAULT_AUTHORITY_NAME = "ROLE_USER";

    private final AuthoritiesRepository authoritiesRepository;

    @Override
    public Authorities getDefaultAuthority() {
        return authoritiesRepository.findByName(DEFAULT_AUTHORITY_NAME)
            .orElse(authoritiesRepository.findById(1L).orElseThrow(() -> new NotFoundException("Authority not found")));
    }

}
