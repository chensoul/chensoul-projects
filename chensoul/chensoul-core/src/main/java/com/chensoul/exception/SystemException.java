package com.chensoul.exception;

import lombok.Getter;

/**
 * System Exception
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class SystemException extends RuntimeException {

    private static final long serialVersionUID = 7750008750467911390L;
    @Getter
    private String message;

    public SystemException(final String message) {
        this.message = message;
    }
}
