package me.oncut.urlshortener.configuration.properties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties("app")
public class AppProperties {

    @NotBlank
    private String frontendUrl;

    @NotBlank
    private String jwtBase64Secret;

    @NotNull
    @Positive
    private Long shortUrlLength;

    @NotNull
    @Positive
    private Long jwtTokenValiditySeconds;

    @NotNull
    @Positive
    private Long apiKeyCallsLimit;

    @NotNull
    @Positive
    private Long apiKeyExpirationInMonths;

    @NotNull
    @Positive
    private Long resetTokenExpirationInHours;

    @NotNull
    @Positive
    private Long userApiKeySlots;

    @NotBlank
    private String emailSenderAddress;

    @NotNull
    @Positive
    private Long inactiveVisitIncrementPerIpInHours;

    @NotNull
    @Positive
    private Long ipRetentionDurationInHours;

    @NotNull
    @Positive
    private Long deactivateUserAccountAfterDays;

    @NotBlank
    private String contactEmail;

    @NotBlank
    private String appName;

    @NotBlank
    private String serverUrl;

    @NotNull
    @Positive
    private Long maxLoginAttempts;
}
