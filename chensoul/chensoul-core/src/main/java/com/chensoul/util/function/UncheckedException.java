package com.chensoul.util.function;

/**
 * Unchecked implementation of {@link java.lang.RuntimeException}.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class UncheckedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an instance initialized to the given {@code cause}.
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A {@code null} value
     *              is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public UncheckedException(final Throwable cause) {
        super(cause);
    }

}
