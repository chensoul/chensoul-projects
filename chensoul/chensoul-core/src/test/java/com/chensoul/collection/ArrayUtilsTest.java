package com.chensoul.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
class ArrayUtilsTest {
    @Test
    void testIsEmpty() {
        Assertions.assertTrue(ArrayUtils.isEmpty(new String[0]));
        Assertions.assertTrue(ArrayUtils.isNotEmpty(new String[1]));

        Assertions.assertTrue(ArrayUtils.length(new Integer[1]) > 0);
    }
}
