package me.oncut.urlshortener.converter;

import me.oncut.urlshortener.dto.CreateUrlDto;
import me.oncut.urlshortener.model.Url;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateUrlToUrlConverter implements Converter<CreateUrlDto, Url> {

  @Override
  public Url convert(final CreateUrlDto createUrlDto) {
    return Url.builder()
        .longUrl(createUrlDto.getLongUrl())
        .shortUrl(createUrlDto.getShortUrl())
        .visitLimit(createUrlDto.getVisitLimit())
        .expirationDate(createUrlDto.getExpirationDate())
        .build();
  }

}
