package com.chensoul.exception;

import com.chensoul.util.Enumerable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ErrorCode implements Enumerable<ErrorCode> {
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
