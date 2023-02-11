package cc.hrva.urlshortener.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlatformType {

    ANY_PLATFORM("ANY_PLATFORM"),
    LINUX("LINUX"),
    OSX("OSX"),
    CHROME("CHROME"),
    ANDROID("ANDROID"),
    IOS("IOS"),
    WINDOWS("WINDOWS");

    private final String value;

}
