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

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
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
    }

    @Test
    public void testCheckedSupplierWithCustomHandler() {
        final CheckedSupplier<Object> supplier = () -> {
            throw new Exception("object");
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        Supplier<Object> alias = CheckedSupplier.unchecked(supplier, handler);

        assertSupplier(alias, IllegalStateException.class);
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

    private <E extends Exception> void assertIntSupplier(IntSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsInt();
            fail();
        } catch (Exception e) {
            assertException(type, e, "int");
        }

        try {
            IntStream.generate(test).findFirst();
        } catch (Exception e) {
            assertException(type, e, "int");
        }
    }

    private <E extends Exception> void assertLongSupplier(LongSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsLong();
            fail();
        } catch (Exception e) {
            assertException(type, e, "long");
        }

        try {
            LongStream.generate(test).findFirst();
        } catch (Exception e) {
            assertException(type, e, "long");
        }
    }

    private <E extends Exception> void assertDoubleSupplier(DoubleSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsDouble();
            fail();
        } catch (Exception e) {
            assertException(type, e, "double");
        }

        try {
            DoubleStream.generate(test).findFirst();
        } catch (Exception e) {
            assertException(type, e, "double");
        }
    }

    private <E extends Exception> void assertBooleanSupplier(BooleanSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsBoolean();
            fail();
        } catch (Exception e) {
            assertException(type, e, "boolean");
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
