package io.mosip.esignet.mock.identitysystem.validator;

import static io.mosip.esignet.mock.identitysystem.util.Constants.UTC_DATETIME_PATTERN;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RequestTimeValidator implements ConstraintValidator<RequestTime, String> {

	@Value("${esignet.mock.identity-system.allowed-request-time-variation-seconds:120}")
    private int timeVariationSeconds;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.isBlank())
            return false;

        try {
            LocalDateTime localDateTime = LocalDateTime.parse(value, DateTimeFormatter.ofPattern(UTC_DATETIME_PATTERN));
            long diff = localDateTime.until(LocalDateTime.now(ZoneOffset.UTC), ChronoUnit.SECONDS);
            return (diff <= timeVariationSeconds && diff >= -timeVariationSeconds);
        } catch (Exception ex) {}

        return false;
    }
}
