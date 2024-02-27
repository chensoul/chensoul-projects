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

import java.util.function.Predicate;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * @author Lukas Eder
 */
public class PredicateTests {

    @Test
    public void testPredicates() {
        Predicate<Integer> even = i -> i % 2 == 0;
        Predicate<Integer> threes = i -> i % 3 == 0;

        assertTrue(even.test(0));
        assertFalse(even.test(1));

        assertFalse(even.negate().test(0));
        assertTrue(even.negate().test(1));

        assertTrue(even.and(threes).test(0));
        assertFalse(even.and(threes).test(1));
        assertFalse(even.and(threes).test(2));
        assertFalse(even.and(threes).test(3));
        assertFalse(even.and(threes).test(4));
        assertFalse(even.and(threes).test(5));
        assertTrue(even.and(threes).test(6));

        assertTrue(even.or(threes).test(0));
        assertFalse(even.or(threes).test(1));
        assertTrue(even.or(threes).test(2));
        assertTrue(even.or(threes).test(3));
        assertTrue(even.or(threes).test(4));
        assertFalse(even.or(threes).test(5));
        assertTrue(even.or(threes).test(6));
    }
}
