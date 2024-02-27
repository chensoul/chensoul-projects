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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.LongFunction;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class CheckedFunctionTest {

    @Test
    public void testCheckedFunction() {
        final CheckedFunction<Object, Object> function = t -> {
            throw new Exception("" + t);
        };

        Function<Object, Object> f1 = CheckedFunction.unchecked(function);
        Function<Object, Object> f2 = CheckedFunction.sneaky(function);

        assertFunction(f1, UncheckedException.class);
        assertFunction(f2, Exception.class);
    }

    @Test
    public void testCheckedFunctionWithCustomHandler() {
        final CheckedFunction<Object, Object> function = t -> {
            throw new Exception("" + t);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        Function<Object, Object> alias = CheckedFunction.unchecked(function, handler);

        assertFunction(alias, IllegalStateException.class);
    }

    private <E extends Exception> void assertFunction(Function<Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(null);
            fail();
        } catch (Exception e) {
            assertException(type, e, "null");
        }

        try {
            Map<Object, Object> map = new LinkedHashMap<>();
            map.computeIfAbsent("a", test);
        } catch (Exception e) {
            assertException(type, e, "a");
        }
    }

    private <E extends Exception> void assertToIntFunction(ToIntFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(null);
            fail();
        } catch (Exception e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("1", "2", "3").mapToInt(test);
        } catch (Exception e) {
            assertException(type, e, "a");
        }
    }

    private <E extends Exception> void assertToLongFunction(ToLongFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(null);
            fail();
        } catch (Exception e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("1", "2", "3").mapToLong(test);
        } catch (Exception e) {
            assertException(type, e, "a");
        }
    }

    private <E extends Exception> void assertToDoubleFunction(ToDoubleFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(null);
            fail();
        } catch (Exception e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("1", "2", "3").mapToDouble(test);
        } catch (Exception e) {
            assertException(type, e, "a");
        }
    }

    private <E extends Exception> void assertIntFunction(IntFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(0);
            fail();
        } catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).mapToObj(test);
        } catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertIntToLongFunction(IntToLongFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(0);
            fail();
        } catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).mapToLong(test);
        } catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertIntToDoubleFunction(IntToDoubleFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(0);
            fail();
        } catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).mapToDouble(test);
        } catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertLongFunction(LongFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(0L);
            fail();
        } catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).mapToObj(test);
        } catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertLongToIntFunction(LongToIntFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(0L);
            fail();
        } catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).mapToInt(test);
        } catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertLongToDoubleFunction(LongToDoubleFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(0L);
            fail();
        } catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).mapToDouble(test);
        } catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertDoubleFunction(DoubleFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(0.0);
            fail();
        } catch (Exception e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).mapToObj(test);
        } catch (Exception e) {
            assertException(type, e, "1.0");
        }
    }

    private <E extends Exception> void assertDoubleToIntFunction(DoubleToIntFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(0.0);
            fail();
        } catch (Exception e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).mapToInt(test);
        } catch (Exception e) {
            assertException(type, e, "1.0");
        }
    }

    private <E extends Exception> void assertDoubleToLongFunction(DoubleToLongFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(0.0);
            fail();
        } catch (Exception e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).mapToLong(test);
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
