package com.chensoul.lang.function;

import java.util.function.Supplier;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class CheckedSupplierTest {

    @Test
    public void testCheckedSupplier() {
        final CheckedSupplier<Object> supplier = () -> {
            throw new Exception("object");
        };

        Supplier<Object> s1 = CheckedSupplier.unchecked(supplier);
        Supplier<Object> s2 = CheckedSupplier.sneaky(supplier);

        assertSupplier(s1, UncheckedException.class);
        assertSupplier(s2, Exception.class);

        Supplier<Object> s3 = CheckedSupplier.unchecked(supplier, e -> {
            throw new IllegalStateException(e);
        });
        assertSupplier(s3, IllegalStateException.class);

    }

    private <E extends Exception> void assertSupplier(Supplier<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.get();
            fail();
        } catch (Exception e) {
            assertException(type, e, "object");
        }

        try {
            Stream.generate(test).findFirst();
        } catch (Exception e) {
            assertException(type, e, "object");
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
