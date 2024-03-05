package com.chensoul.reflect;

import com.chensoul.lang.function.CheckedConsumer;
import com.chensoul.lang.function.CheckedFunction;
import com.chensoul.lang.function.CheckedSupplier;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * The utilities class of {@link AccessibleObject}
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
public abstract class AccessibleObjectUtils {

    /**
     * Execute an {@link AccessibleObject} instance
     *
     * @param object   {@link AccessibleObject} instance, {@link Field}, {@link Method} or {@link Constructor}
     * @param callback the call back to execute {@link AccessibleObject} object
     * @param <A>      The type or subtype of {@link AccessibleObject}
     */
    public static <A extends AccessibleObject> void execute(A object, CheckedConsumer<A> callback) {
        execute(object, a -> {
            callback.accept(a);
            return null;
        });
    }

    /**
     * Executes the {@link AccessibleObject}
     *
     * @param accessibleObject {@link AccessibleObject}
     * @param supplier         {@link CheckedSupplier}
     * @throws RuntimeException if execution failed
     */
    public static <A extends AccessibleObject, R> R execute(A accessibleObject, CheckedSupplier<R> supplier) {
        return execute(accessibleObject, (CheckedFunction<A, R>) a -> supplier.get());
    }

    /**
     * Execute an {@link AccessibleObject} instance
     *
     * @param accessibleObject {@link AccessibleObject} instance, {@link Field}, {@link Method} or {@link Constructor}
     * @param callback         the call back to execute {@link AccessibleObject} accessibleObject
     * @param <A>              The type or subtype of {@link AccessibleObject}
     * @param <R>              The type of execution result
     * @return The execution result
     * @throws NullPointerException If <code>accessibleObject</code> is <code>null</code>
     */
    public static <A extends AccessibleObject, R> R execute(A accessibleObject, CheckedFunction<A, R> callback) throws NullPointerException {
        boolean accessible = accessibleObject.isAccessible();
        final R result;
        try {
            if (!accessible) {
                accessibleObject.setAccessible(true);
            }
            result = CheckedFunction.unchecked(callback).apply(accessibleObject);
        } finally {
            if (!accessible) {
                accessibleObject.setAccessible(accessible);
            }
        }
        return result;
    }
}
