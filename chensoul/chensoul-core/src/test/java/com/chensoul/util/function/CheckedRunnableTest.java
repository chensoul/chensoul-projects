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

import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class CheckedRunnableTest {

    @Test
    public void testCheckedRunnable() {
        final CheckedRunnable runnable = () -> {
            throw new Exception("runnable");
        };

        Runnable r1 = CheckedRunnable.unchecked(runnable);
        Runnable r2 = CheckedRunnable.sneaky(runnable);

        assertRunnable(r1, UncheckedException.class);
        assertRunnable(r2, Exception.class);
    }

    @Test
    public void testCheckedRunnableWithCustomHandler() {
        final CheckedRunnable runnable = () -> {
            throw new Exception("runnable");
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        Runnable alias = CheckedRunnable.unchecked(runnable, handler);

        assertRunnable(alias, IllegalStateException.class);
    }

    private <E extends Exception> void assertRunnable(Runnable test, Class<E> type) {
        assertNotNull(test);
        try {
            test.run();
            fail();
        } catch (Exception e) {
            assertException(type, e, "runnable");
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

    @Test
    public void testCheckedRunnableRethrowAll() {
        Runnable test = CheckedRunnable.unchecked(
            () -> {
                throw new Throwable("runnable");
            },
            FunctionUtils.RETHROW_ALL
        );

        try {
            test.run();
            fail();
        } catch (Throwable e) {
            assertEquals("runnable", e.getMessage());
        }
    }
}
