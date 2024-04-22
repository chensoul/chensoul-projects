package com.chensoul.reflect;

import com.chensoul.lang.function.CheckedConsumer;
import com.chensoul.lang.function.CheckedFunction;
import com.chensoul.lang.function.CheckedSupplier;
import com.chensoul.lang.function.FunctionUtils;
import java.lang.reflect.AccessibleObject;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
public abstract class AccessibleObjectUtils {
    public static <A extends AccessibleObject> void execute(A accessibleObject, CheckedConsumer<A> consumer) {
        execute(accessibleObject, a -> {
            consumer.accept(a);
            return null;
        });
    }

    /**
     * Execute an {@link java.lang.reflect.AccessibleObject} instance
     *
     * @param accessibleObject {@link java.lang.reflect.AccessibleObject} instance, {@link java.lang.reflect.Field}, {@link java.lang.reflect.Method} or {@link java.lang.reflect.Constructor}
     * @param supplier         the supplier to execute {@link java.lang.reflect.AccessibleObject} object
     * @return {@link R}       the execution result
     * @param <A> a A class
     * @param <R> a R class
     */
    public static <A extends AccessibleObject, R> R execute(A accessibleObject, CheckedSupplier<R> supplier) {
        return execute(accessibleObject, (CheckedFunction<A, R>) a -> supplier.get());
    }

    /**
     * Execute an {@link java.lang.reflect.AccessibleObject} instance
     *
     * @param accessibleObject {@link java.lang.reflect.AccessibleObject} instance, {@link java.lang.reflect.Field}, {@link java.lang.reflect.Method} or {@link java.lang.reflect.Constructor}
     * @param function         the function to execute {@link java.lang.reflect.AccessibleObject} object
     * @return {@link R}       the execution result
     * @param <A> a A class
     * @param <R> a R class
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
