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

import java.util.function.Supplier;
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

        Supplier<Object> s3 = CheckedSupplier.unchecked(supplier, e -> {
            throw new IllegalStateException(e);
        });
        assertSupplier(s3, IllegalStateException.class);

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
