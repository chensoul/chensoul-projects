package com.chensoul.lang.function;

import java.util.function.Predicate;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
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
