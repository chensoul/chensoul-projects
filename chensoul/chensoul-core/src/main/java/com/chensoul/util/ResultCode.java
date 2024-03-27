package com.chensoul.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public enum ResultCode implements Enumerable<ResultCode> {
    SUCCESS(0, "SUCCESS"),

    BAD_REQUEST(400, "BAD_REQUEST"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    FORBIDDEN(403, "FORBIDDEN"),
    NOT_FOUND(404, "NOT_FOUND"),
    TOO_MANY_REQUESTS(429, "TOO_MANY_REQUESTS"),

    SYSTEM_ERROR(500, "INTERNAL_ERROR"),
    SERVICE_ERROR(501, "SERVICE_ERROR"),
    SERVICE_UNAVAILABLE(502, "SERVICE_UNAVAILABLE"),
    ;

    private int code;
    private String name;

}
