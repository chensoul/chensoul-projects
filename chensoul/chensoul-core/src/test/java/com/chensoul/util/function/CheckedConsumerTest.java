/**
 * Copyright (c), Data Geekery GmbH, contact@datageekery.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chensoul.util.function;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
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
    }

    @Test
    public void testCheckedConsumerWithCustomHandler() {
        final CheckedConsumer<Object> consumer = o -> {
            throw new Exception("" + o);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        Consumer<Object> alias = CheckedConsumer.unchecked(consumer, handler);

        assertConsumer(alias, IllegalStateException.class);
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

    private <E extends Exception> void assertIntConsumer(IntConsumer test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(0);
            fail();
        } catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            Arrays.stream(new int[]{1, 2, 3}).forEach(test);
        } catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertLongConsumer(LongConsumer test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(0L);
            fail();
        } catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            Arrays.stream(new long[]{1L, 2L, 3L}).forEach(test);
        } catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertDoubleConsumer(DoubleConsumer test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(0.0);
            fail();
        } catch (Exception e) {
            assertException(type, e, "0.0");
        }

        try {
            Arrays.stream(new double[]{1.0, 2.0, 3.0}).forEach(test);
        } catch (Exception e) {
            assertException(type, e, "1.0");
        }
    }

    private <E extends Exception> void assertException(Class<E> type, Exception e, String message) {
        assertEquals(type, e.getClass());

        // Sneaky
        if (e.getCause() == null) {
            assertEquals(message, e.getMessage());
        }

        // Unchecked
        else {
            assertEquals(Exception.class, e.getCause().getClass());
            assertEquals(message, e.getCause().getMessage());
        }
    }
}
