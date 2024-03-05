package com.chensoul.reflect;

import com.chensoul.lang.function.CheckedSupplier;
import com.chensoul.lang.function.Streams;
import java.lang.reflect.Constructor;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.function.Predicate;

/**
 * The utilities class of {@link Constructor}
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public abstract class ConstructorUtils {

    /**
     * Is a non-private constructor without parameters
     *
     * @param constructor {@link Constructor}
     * @return <code>true</code> if the given {@link Constructor} is a public no-arg one,
     * otherwise <code>false</code>
     */
    public static boolean isNonPrivateConstructorWithoutParameters(Constructor<?> constructor) {
        return !MemberUtils.isPrivate(constructor) && constructor.getParameterCount() < 1;
    }

    public static boolean hasNonPrivateConstructorWithoutParameters(Class<?> type) {
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        boolean has = false;
        for (Constructor<?> constructor : constructors) {
            if (isNonPrivateConstructorWithoutParameters(constructor)) {
                has = true;
                break;
            }
        }
        return has;
    }

    public static List<Constructor<?>> getConstructors(Class<?> type,
                                                       Predicate<? super Constructor<?>>... constructorFilters) {
        List<Constructor<?>> constructors = asList(type.getConstructors());
        return Streams.filterAll(constructors, constructorFilters);
    }

    public static List<Constructor<?>> getDeclaredConstructors(Class<?> type,
                                                               Predicate<? super Constructor<?>>... constructorFilters) {
        List<Constructor<?>> constructors = asList(type.getDeclaredConstructors());
        return Streams.filterAll(constructors, constructorFilters);
    }

    public static <T> Constructor<T> getConstructor(Class<T> type, Class<?>... parameterTypes) {
        return CheckedSupplier.unchecked(() -> type.getConstructor(parameterTypes)).get();
    }

    public static <T> Constructor<T> getDeclaredConstructor(Class<T> type, Class<?>... parameterTypes) {
        return CheckedSupplier.unchecked(() -> type.getDeclaredConstructor(parameterTypes)).get();
    }

    /**
     * Create an instance by the specified {@link Constructor} and arguments
     *
     * @param constructor {@link Constructor}
     * @param args        the {@link Constructor Constructors} arguments
     * @param <T>         the type of instance
     * @return non-null
     */
    public static <T> T newInstance(Constructor<T> constructor, Object... args) {
        return AccessibleObjectUtils.execute(constructor, () -> constructor.newInstance(args));
    }
}
