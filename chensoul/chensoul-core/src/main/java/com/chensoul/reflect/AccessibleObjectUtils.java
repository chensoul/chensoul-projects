package com.chensoul.reflect;

import com.chensoul.lang.function.CheckedConsumer;
import com.chensoul.lang.function.CheckedFunction;
import com.chensoul.lang.function.CheckedSupplier;
import com.chensoul.lang.function.FunctionUtils;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * The utilities class of {@link AccessibleObject} to execute an  {@link AccessibleObject} instance, such as {@link Field}, {@link Method} or {@link Constructor}
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
public abstract class AccessibleObjectUtils {

    /**
     * Execute an {@link AccessibleObject} instance
     *
     * @param accessibleObject {@link AccessibleObject} instance, {@link Field}, {@link Method} or {@link Constructor}
     * @param consumer         the consumer to execute {@link AccessibleObject} object
     */
    public static <A extends AccessibleObject> void execute(A accessibleObject, CheckedConsumer<A> consumer) {
        execute(accessibleObject, a -> {
            consumer.accept(a);
            return null;
        });
    }

    /**
     * Execute an {@link AccessibleObject} instance
     *
     * @param accessibleObject {@link AccessibleObject} instance, {@link Field}, {@link Method} or {@link Constructor}
     * @param supplier         the supplier to execute {@link AccessibleObject} object
     * @return {@link R}       the execution result
     */
    public static <A extends AccessibleObject, R> R execute(A accessibleObject, CheckedSupplier<R> supplier) {
        return execute(accessibleObject, (CheckedFunction<A, R>) a -> supplier.get());
    }

    /**
     * Execute an {@link AccessibleObject} instance
     *
     * @param accessibleObject {@link AccessibleObject} instance, {@link Field}, {@link Method} or {@link Constructor}
     * @param function         the function to execute {@link AccessibleObject} object
     * @return {@link R}       the execution result
     */
    public static <A extends AccessibleObject, R> R execute(A accessibleObject, CheckedFunction<A, R> function) {
        boolean accessible = accessibleObject.isAccessible();

        if (!accessible) {
            accessibleObject.setAccessible(true);
        }

        return FunctionUtils.tryApply(function, t -> {
            accessibleObject.setAccessible(accessible);
        }).apply(accessibleObject);
    }
}
