package me.oncut.urlshortener.converter;

import lombok.RequiredArgsConstructor;
import me.oncut.urlshortener.dto.UrlUpdateDto;
import me.oncut.urlshortener.exception.UrlNotFoundException;
import me.oncut.urlshortener.model.Url;
import me.oncut.urlshortener.repository.UrlRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UrlUpdateDtoToUrlConverter implements Converter<UrlUpdateDto, Url> {

    private final UrlRepository urlRepository;

    @Override
    public Url convert(final UrlUpdateDto urlUpdateDto) {
        final Url existingUrl = urlRepository.findById(urlUpdateDto.getId())
                .orElseThrow(() -> new UrlNotFoundException(String.format("Url with id %d doesn't exist", urlUpdateDto.getId())));
        existingUrl.setVisitLimit(urlUpdateDto.getVisitLimit());
        return existingUrl;
    }

}
