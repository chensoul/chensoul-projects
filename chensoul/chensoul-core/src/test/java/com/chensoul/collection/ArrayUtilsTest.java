package com.chensoul.collection;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
class ArrayUtilsTest {
    @Test
    void testIsEmpty() {
        Assertions.assertTrue(ArrayUtils.isEmpty(new String[0]));
        Assertions.assertTrue(ArrayUtils.isNotEmpty(new String[1]));

        Assertions.assertTrue(ArrayUtils.getLength(new Integer[1]) > 0);
    }
}
