package com.chensoul.lang.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class CheckedRunnableTest {

    @Test
    public void testCheckedRunnable() {
        final CheckedRunnable runnable = () -> {
            throw new Exception("runnable");
        };

        Runnable r1 = CheckedRunnable.unchecked(runnable);
        Runnable r2 = CheckedRunnable.sneaky(runnable);

        assertRunnable(r1, UncheckedException.class);
        assertRunnable(r2, Exception.class);

        Runnable r3 = CheckedRunnable.unchecked(runnable, e -> {
            throw new IllegalStateException(e);
        });
        assertRunnable(r3, IllegalStateException.class);
    }

    private <E extends Exception> void assertRunnable(Runnable test, Class<E> type) {
        assertNotNull(test);
        try {
            test.run();
            fail();
        } catch (Exception e) {
            assertException(type, e, "runnable");
        }
    }

    private <E extends Exception> void assertException(Class<E> type, Exception e, String message) {
        assertEquals(type, e.getClass());

        // Sneaky
        if (e.getCause() == null) {
            assertEquals(message, e.getMessage());
        } else {
            assertEquals(Exception.class, e.getCause().getClass());
            assertEquals(message, e.getCause().getMessage());
        }
    }

    @Test
    public void testCheckedRunnableRethrowAll() {
        Runnable test = CheckedRunnable.unchecked(
            () -> {
                throw new Throwable("runnable");
            },
            FunctionUtils.SNEAKY_THROW
        );

        try {
            test.run();
            fail();
        } catch (Throwable e) {
            assertEquals("runnable", e.getMessage());
        }
    }
}
