package com.chensoul.util.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import lombok.val;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * This is {@link FunctionUtilsTest}.
 */
@Tag("Utility")
class FunctionUtilsTest {

    @Test
    void verifyDoIf0() throws Throwable {
        val result = new AtomicBoolean();
        FunctionUtils.doIf(true, t -> result.set(true), s -> result.set(false)).accept("input");
        assertTrue(result.get());

        FunctionUtils.doIf(false, t -> result.set(true), s -> result.set(false)).accept("input");
        assertFalse(result.get());
    }

    @Test
    void verifyDoAndHandle() throws Throwable {
        assertThrows(IllegalArgumentException.class,
            () -> FunctionUtils.doAndHandle((CheckedFunction<Object, Boolean>) o -> {
                throw new IllegalArgumentException();
            }, o -> {
                throw new IllegalArgumentException();
            }).apply(Void.class));

        assertFalse(FunctionUtils.doAndHandle((CheckedFunction<Object, Boolean>) o -> {
            throw new IllegalArgumentException();
        }, o -> false).apply(Void.class));
    }

    @Test
    void verifyDoAndHandle2() throws Throwable {
        Supplier supplier = FunctionUtils.doAndHandle(
            () -> {
                throw new IllegalArgumentException();
            }, o -> {
                throw new IllegalArgumentException();
            });
        assertThrows(IllegalArgumentException.class, supplier::get);
        supplier = FunctionUtils.doAndHandle(
            () -> {
                throw new IllegalArgumentException();
            }, o -> false);
        assertFalse((Boolean) supplier.get());
    }

    @Test
    void sneakyThrow() {
        assertThrowsExactly(Throwable.class, () -> FunctionUtils.sneakyThrow(new Throwable()));
        assertThrowsExactly(RuntimeException.class, () -> FunctionUtils.sneakyThrow(new RuntimeException()));
    }

    @Test
    void doUnchecked() {
        assertEquals(1, FunctionUtils.doUnchecked(() -> 1));

        RuntimeException runtimeException = new RuntimeException();
        assertEquals(runtimeException, FunctionUtils.doUnchecked(() -> runtimeException));

        assertThrows(RuntimeException.class, () -> FunctionUtils.doUnchecked(() -> {
            throw new RuntimeException();
        }));

        assertThrows(UncheckedIOException.class, () -> FunctionUtils.doUnchecked(() -> {
            throw new IOException();
        }));

        assertThrows(UncheckedException.class, () -> FunctionUtils.doUnchecked(() -> {
            throw new Exception();
        }));
    }

    @Test
    void testDoUnchecked() {
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        FunctionUtils.doUnchecked(o -> {
            atomicBoolean.set(true);
        }, 1);

        assertTrue(atomicBoolean.get());
    }

    @Test
    void doIf() {
    }

    @Test
    void testDoIf() {
    }

    @Test
    void testDoIf1() {
    }

    @Test
    void testDoIf2() {
    }

    @Test
    void testDoIf3() {
    }

    @Test
    void doAndHandle() {
    }

    @Test
    void testDoAndHandle() {
    }

    @Test
    void testDoAndHandle1() {
    }

    @Test
    void testDoAndHandle2() {
    }

    @Test
    void doAndRetry() {
    }

    @Test
    void testDoAndRetry() {
    }

    @Test
    void throwIfBlank() {
    }

    @Test
    void throwIfNull() {
    }

    @Test
    void throwIf() {
    }
}
