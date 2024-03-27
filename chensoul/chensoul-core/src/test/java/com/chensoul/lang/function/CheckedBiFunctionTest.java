package com.chensoul.lang.function;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * @author Lukas Eder
 */
public class CheckedBiFunctionTest {

    @Test
    public void testCheckedBiFunction() {
        final CheckedBiFunction<Object, Object, Object> biFunction = (t, u) -> {
            throw new Exception(t + ":" + u);
        };

        BiFunction<Object, Object, Object> f1 = CheckedBiFunction.unchecked(biFunction);
        BiFunction<Object, Object, Object> f2 = CheckedBiFunction.sneaky(biFunction);

        assertBiFunction(f1, UncheckedException.class);
        assertBiFunction(f2, Exception.class);
    }

    @Test
    public void testCheckedBiFunctionWithCustomHandler() {
        final CheckedBiFunction<Object, Object, Object> biFunction = (t, u) -> {
            throw new Exception(t + ":" + u);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        BiFunction<Object, Object, Object> alias = CheckedBiFunction.unchecked(biFunction, handler);
        assertBiFunction(alias, IllegalStateException.class);
    }

    private <E extends Exception> void assertBiFunction(BiFunction<Object, Object, Object> test, Class<E> type) {
        assertNotNull(test);

        try {
            test.apply(null, null);
            fail();
        } catch (Exception e) {
            assertException(type, e, "null:null");
        }

        try {
            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("a", "b");
            map.computeIfPresent("a", test);
        } catch (Exception e) {
            assertException(type, e, "a:b");
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
