package cc.hrva.urlshortener.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ThreatType {

    MALWARE("MALWARE"),
    SOCIAL_ENGINEERING("SOCIAL_ENGINEERING"),
    UNWANTED_SOFTWARE("UNWANTED_SOFTWARE"),
    POTENTIALLY_HARMFUL_APPLICATION("POTENTIALLY_HARMFUL_APPLICATION");

    private final String value;

}
