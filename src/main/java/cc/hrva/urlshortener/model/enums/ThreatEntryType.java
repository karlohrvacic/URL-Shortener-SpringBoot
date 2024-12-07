package cc.hrva.urlshortener.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ThreatEntryType {

    URL("URL"),
    IP_RANGE("IP_RANGE");

    private final String value;

}
