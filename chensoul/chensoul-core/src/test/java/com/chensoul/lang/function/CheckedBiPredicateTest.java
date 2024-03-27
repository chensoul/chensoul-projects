package com.chensoul.lang.function;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class CheckedBiPredicateTest {

    @Test
    public void testCheckedBiPredicate() {
        final CheckedBiPredicate<Object, Object> biPredicate = (t, u) -> {
            throw new Exception(t + ":" + u);
        };

        BiPredicate<Object, Object> p1 = CheckedBiPredicate.unchecked(biPredicate);
        BiPredicate<Object, Object> p2 = CheckedBiPredicate.sneaky(biPredicate);

        assertBiPredicate(p1, UncheckedException.class);
        assertBiPredicate(p2, Exception.class);
    }

    @Test
    public void testCheckedBiPredicateWithCustomHandler() {
        final CheckedBiPredicate<Object, Object> biPredicate = (t, u) -> {
            throw new Exception(t + ":" + u);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        BiPredicate<Object, Object> alias = CheckedBiPredicate.unchecked(biPredicate, handler);
        assertBiPredicate(alias, IllegalStateException.class);
    }

    private <E extends Exception> void assertBiPredicate(BiPredicate<Object, Object> test, Class<E> type) {
        assertNotNull(test);

        try {
            test.test(null, null);
            fail();
        } catch (Exception e) {
            assertException(type, e, "null:null");
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
