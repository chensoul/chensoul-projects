package com.chensoul.jackson.serializer.sanitizer;

import static com.chensoul.jackson.serializer.sanitizer.Sanitizer.SanitizerType.PASSWORD;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = StringSanitizerSerialize.class)
public @interface Sanitizer {
    SanitizerType type() default PASSWORD;

    enum SanitizerType {
        ADDRESS,
        BANK_CARD,
        CHINESE_NAME,
        CAR_NUMBER,
        EMAIL,
        ID_CARD,
        PHONE,
        PASSWORD;
    }
}
