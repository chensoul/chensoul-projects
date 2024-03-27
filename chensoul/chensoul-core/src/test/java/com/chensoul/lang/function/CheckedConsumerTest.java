package com.chensoul.lang.function;

import java.util.Arrays;
import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class CheckedConsumerTest {

    @Test
    public void testCheckedConsumer() {
        final CheckedConsumer<Object> consumer = o -> {
            throw new Exception("" + o);
        };

        Consumer<Object> c1 = CheckedConsumer.unchecked(consumer);
        Consumer<Object> c2 = CheckedConsumer.sneaky(consumer);

        assertConsumer(c1, UncheckedException.class);
        assertConsumer(c2, Exception.class);

        Consumer<Object> c3 = CheckedConsumer.unchecked(consumer, e -> {
            throw new IllegalStateException(e);
        });

        assertConsumer(c3, IllegalStateException.class);
    }

    private <E extends Exception> void assertConsumer(Consumer<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(null);
            fail();
        } catch (Exception e) {
            assertException(type, e, "null");
        }

        try {
            Arrays.asList("a", "b", "c").stream().forEach(test);
        } catch (Exception e) {
            assertException(type, e, "a");
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
}
