package me.oncut.urlshortener.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeekUrl {

  private String longUrl;

  private String shortUrl;

  private LocalDateTime createDate;

}
