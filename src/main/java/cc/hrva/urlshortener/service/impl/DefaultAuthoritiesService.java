package cc.hrva.urlshortener.service.impl;

import cc.hrva.urlshortener.repository.AuthoritiesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import cc.hrva.urlshortener.model.codebook.Authorities;
import cc.hrva.urlshortener.service.AuthoritiesService;
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
