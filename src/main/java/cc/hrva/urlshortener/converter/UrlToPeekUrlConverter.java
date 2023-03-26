package cc.hrva.urlshortener.converter;

import cc.hrva.urlshortener.model.Url;
import cc.hrva.urlshortener.model.PeekUrl;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UrlToPeekUrlConverter implements Converter<Url, PeekUrl> {

    @Override
    public PeekUrl convert(final Url url) {
        return PeekUrl.builder()
                .longUrl(url.getLongUrl())
                .shortUrl(url.getShortUrl())
                .createDate(url.getCreateDate())
                .build();
    }

}
