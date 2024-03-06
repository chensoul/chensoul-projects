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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
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
