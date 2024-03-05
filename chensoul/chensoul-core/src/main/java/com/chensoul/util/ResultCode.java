package com.chensoul.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Result code
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements Enumerable<ResultCode> {
    SUCCESS(0, "SUCCESS"),

    BAD_REQUEST(400, "BAD_REQUEST"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    FORBIDDEN(403, "FORBIDDEN"),

    SYSTEM_ERROR(500, "INTERNAL_ERROR"),
    SERVICE_ERROR(501, "SERVICE_ERROR"),
    SERVICE_UNAVAILABLE(502, "SERVICE_UNAVAILABLE"),
    ;

    private int code;
    private String name;

}
