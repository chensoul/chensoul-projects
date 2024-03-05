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
package com.chensoul.lang.function;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class CheckedBiConsumerTest {

    @Test
    public void testCheckedBiConsumer() {
        final CheckedBiConsumer<Object, Object> biConsumer = (o1, o2) -> {
            throw new Exception(o1 + ":" + o2);
        };

        BiConsumer<Object, Object> c1 = CheckedBiConsumer.unchecked(biConsumer);
        BiConsumer<Object, Object> c2 = CheckedBiConsumer.sneaky(biConsumer);

        assertBiConsumer(c1, UncheckedException.class);
        assertBiConsumer(c2, Exception.class);
    }

    @Test
    public void testCheckedBiConsumerWithCustomHandler() {
        final CheckedBiConsumer<Object, Object> biConsumer = (o1, o2) -> {
            throw new Exception(o1 + ":" + o2);
        };

        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        BiConsumer<Object, Object> c1 = CheckedBiConsumer.unchecked(biConsumer, handler);

        assertBiConsumer(c1, IllegalStateException.class);
    }

    private <E extends Exception> void assertBiConsumer(BiConsumer<Object, Object> test, Class<E> type) {
        assertNotNull(test);

        try {
            test.accept(null, null);
            fail();
        } catch (Exception e) {
            assertException(type, e, "null:null");
        }

        try {
            Map<String, Integer> map = new LinkedHashMap<>();
            map.put("a", 1);
            map.put("b", 2);
            map.put("c", 3);
            map.forEach(test);
        } catch (Exception e) {
            assertException(type, e, "a:1");
        }
    }

    private <E extends Exception> void assertException(Class<E> type, Throwable e, String message) {
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
