package com.chensoul.lang.function;

import java.util.Comparator;
import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class CheckedComparatorTest {

    @Test
    public void testCheckedComparator() {
        final CheckedComparator<Object> comparator = (t1, t2) -> {
            throw new Exception(t1 + ":" + t2);
        };

        Comparator<Object> c1 = CheckedComparator.unchecked(comparator);
        Comparator<Object> c2 = CheckedComparator.sneaky(comparator);

        assertComparator(c1, UncheckedException.class);
        assertComparator(c2, Exception.class);
    }

    @Test
    public void testCheckedComparatorWithCustomHandler() {
        final CheckedComparator<Object> comparator = (t1, t2) -> {
            throw new Exception(t1 + ":" + t2);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        Comparator<Object> alias = CheckedComparator.unchecked(comparator, handler);

        assertComparator(alias, IllegalStateException.class);
    }

    private <E extends Exception> void assertComparator(Comparator<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.compare(null, null);
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
