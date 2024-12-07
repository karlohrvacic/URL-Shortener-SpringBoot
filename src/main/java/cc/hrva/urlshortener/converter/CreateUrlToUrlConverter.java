package cc.hrva.urlshortener.converter;

import cc.hrva.urlshortener.dto.CreateUrlDto;
import cc.hrva.urlshortener.model.Url;
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
