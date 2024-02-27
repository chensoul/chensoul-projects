package com.alibaba.csp.sentinel.dashboard.client;

/**
 * @author Eric Zhao
 */
public class CommandFailedException extends RuntimeException {

    private static final long serialVersionUID = 1539990651823923245L;

    public CommandFailedException() {
    }

    public CommandFailedException(final String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
