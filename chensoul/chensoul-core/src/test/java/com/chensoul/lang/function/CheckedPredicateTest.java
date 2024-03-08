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

import java.util.function.Predicate;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class CheckedPredicateTest {

    @Test
    public void testCheckedPredicate() {
        final CheckedPredicate<Object> predicate = t -> {
            throw new Exception("" + t);
        };

        Predicate<Object> p1 = CheckedPredicate.unchecked(predicate);
        Predicate<Object> p2 = CheckedPredicate.sneaky(predicate);

        assertPredicate(p1, UncheckedException.class);
        assertPredicate(p2, Exception.class);

        Predicate<Object> p3 = CheckedPredicate.unchecked(predicate, e -> {
            throw new IllegalStateException(e);
        });

        assertPredicate(p3, IllegalStateException.class);
    }

    private <E extends Exception> void assertPredicate(Predicate<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.test(null);
            fail();
        } catch (Exception e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("a", "b", "c").filter(test);
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
