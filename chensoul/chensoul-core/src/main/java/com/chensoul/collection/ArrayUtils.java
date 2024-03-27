//package com.chensoul.collection;
//
//import static com.chensoul.collection.ListUtils.newArrayList;
//import java.lang.reflect.Array;
//import static java.lang.reflect.Array.newInstance;
//import java.lang.reflect.Parameter;
//import java.util.Collection;
//import static java.util.Collections.list;
//import java.util.Enumeration;
//import java.util.Objects;
//import java.util.function.BiConsumer;
//import java.util.function.Consumer;
//import java.util.stream.Stream;
////public abstract class ArrayUtils {
////    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
//
//    /**
//     * An empty immutable {@code Class} array.
//     */
//    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
//
//    /**
//     * An empty immutable {@code String} array.
//     */
//    public static final String[] EMPTY_STRING_ARRAY = new String[0];
//
//    /**
//     * An empty immutable {@code Parameter} array.
//     */
//    public static final Parameter[] EMPTY_PARAMETER_ARRAY = new Parameter[0];
//
//    /**
//     * An empty immutable {@code long} array.
//     */
//    public static final long[] EMPTY_LONG_ARRAY = new long[0];
//
//    /**
//     * An empty immutable {@code Long} array.
//     */
//    public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
//
//    /**
//     * An empty immutable {@code int} array.
//     */
//    public static final int[] EMPTY_INT_ARRAY = new int[0];
//
//    /**
//     * An empty immutable {@code Integer} array.
//     */
//    public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
//
//    /**
//     * An empty immutable {@code short} array.
//     */
//    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
//
//    /**
//     * An empty immutable {@code Short} array.
//     */
//    public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
//
//    /**
//     * An empty immutable {@code byte} array.
//     */
//    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
//
//    /**
//     * An empty immutable {@code Byte} array.
//     */
//    public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
//
//    /**
//     * An empty immutable {@code double} array.
//     */
//    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
//
//    /**
//     * An empty immutable {@code Double} array.
//     */
//    public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
//
//    /**
//     * An empty immutable {@code float} array.
//     */
//    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
//
//    /**
//     * An empty immutable {@code Float} array.
//     */
//    public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
//
//    /**
//     * An empty immutable {@code boolean} array.
//     */
//    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
//
//    /**
//     * An empty immutable {@code Boolean} array.
//     */
//    public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
//
//    /**
//     * An empty immutable {@code char} array.
//     */
//    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
//
//    /**
//     * An empty immutable {@code Character} array.
//     */
//    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
//
//    /**
//     * <p>length.</p>
//     *
//     * @param values an array of T[] objects
//     * @param <T> a T class
//     * @return a int
//     */
//    public static <T> int length(T[] values) {
//        return values == null ? 0 : values.length;
//    }
//
//    /**
//     * <p>isEmpty.</p>
//     *
//     * @param values an array of T[] objects
//     * @param <T> a T class
//     * @return a boolean
//     */
//    public static <T> boolean isEmpty(T[] values) {
//        return length(values) == 0;
//    }
//
//    /**
//     * <p>isNotEmpty.</p>
//     *
//     * @param values an array of T[] objects
//     * @param <T> a T class
//     * @return a boolean
//     */
//    public static <T> boolean isNotEmpty(T[] values) {
//        return !isEmpty(values);
//    }
//
//    /**
//     * <p>of.</p>
//     *
//     * @param values a T object
//     * @param <T> a T class
//     * @return an array of T[] objects
//     */
//    public static <T> T[] of(T... values) {
//        return values;
//    }
//
//    /**
//     * <p>isArray.</p>
//     *
//     * @param object a {@link java.lang.Object} object
//     * @return a boolean
//     */
//    public static boolean isArray(final Object object) {
//        return object != null && object.getClass().isArray();
//    }
//
//    /**
//     * <p>asArray.</p>
//     *
//     * @param enumeration a {@link java.util.Enumeration} object
//     * @param componentType a {@link java.lang.Class} object
//     * @param <E> a E class
//     * @return an array of E[] objects
//     */
//    public static <E> E[] asArray(Enumeration<E> enumeration, Class<?> componentType) {
//        return asArray(list(enumeration), componentType);
//    }
//
//    /**
//     * <p>asArray.</p>
//     *
//     * @param elements a {@link java.lang.Iterable} object
//     * @param componentType a {@link java.lang.Class} object
//     * @param <E> a E class
//     * @return an array of E[] objects
//     */
//    public static <E> E[] asArray(Iterable<E> elements, Class<?> componentType) {
//        return asArray(newArrayList(elements), componentType);
//    }
//
//    /**
//     * <p>asArray.</p>
//     *
//     * @param collection a {@link java.util.Collection} object
//     * @param componentType a {@link java.lang.Class} object
//     * @param <E> a E class
//     * @return an array of E[] objects
//     */
//    public static <E> E[] asArray(Collection<E> collection, Class<?> componentType) {
//        return collection.toArray(newArray(componentType, 0));
//    }
//
//    /**
//     * <p>newArray.</p>
//     *
//     * @param componentType a {@link java.lang.Class} object
//     * @param length a int
//     * @param <E> a E class
//     * @return an array of E[] objects
//     */
//    public static <E> E[] newArray(Class<?> componentType, int length) {
//        return (E[]) newInstance(componentType, length);
//    }
//
//    /**
//     * <p>combine.</p>
//     *
//     * @param one a E object
//     * @param others a E object
//     * @param <E> a E class
//     * @return an array of E[] objects
//     */
//    public static <E> E[] combine(E one, E... others) {
//        int othersLength = length(others);
//        Class<?> oneType = one.getClass();
//        boolean oneIsArray = isArray(oneType);
//
//        if (oneIsArray) {
//            return combineArray((E[]) oneType.cast(one), others);
//        } else {
//            Class<?> componentType = oneType;
//            int length = 1 + othersLength;
//            E[] values = newArray(componentType, length);
//            values[0] = one;
//            System.arraycopy(others, 0, values, 1, othersLength);
//            return values;
//        }
//    }
//
//    /**
//     * <p>combine.</p>
//     *
//     * @param one an array of E[] objects
//     * @param others an array of E[] objects
//     * @param <E> a E class
//     * @return an array of E[] objects
//     */
//    public static <E> E[] combine(E[] one, E[]... others) {
//        return combineArray(one, others);
//    }
//
//    /**
//     * <p>combineArray.</p>
//     *
//     * @param one an array of E[] objects
//     * @param others an array of E[] objects
//     * @param <E> a E class
//     * @return an array of E[] objects
//     */
//    public static <E> E[] combineArray(E[] one, E[]... others) {
//        int othersSize = length(others);
//        if (othersSize < 1) {
//            return one;
//        }
//        int oneSize = length(one);
//        int size = oneSize;
//
//        for (int i = 0; i < othersSize; i++) {
//            E[] other = others[i];
//            int otherLength = length(other);
//            size += otherLength;
//        }
//
//        Class<?> componentType = one.getClass().getComponentType();
//        E[] newArray = newArray(componentType, size);
//
//        int pos = 0;
//        System.arraycopy(one, 0, newArray, pos, oneSize);
//        pos += oneSize;
//
//        for (int i = 0; i < othersSize; i++) {
//            E[] other = others[i];
//            int otherLength = length(other);
//            if (otherLength > 0) {
//                System.arraycopy(other, 0, newArray, pos, otherLength);
//                pos += otherLength;
//            }
//        }
//        return newArray;
//    }
//
//    /**
//     * <p>forEach.</p>
//     *
//     * @param values an array of T[] objects
//     * @param indexedElementConsumer a {@link java.util.function.BiConsumer} object
//     * @param <T> a T class
//     */
//    public static <T> void forEach(T[] values, BiConsumer<Integer, T> indexedElementConsumer) {
//        int length = length(values);
//        for (int i = 0; i < length; i++) {
//            T value = values[i];
//            indexedElementConsumer.accept(i, value);
//        }
//    }
//
//    /**
//     * <p>forEach.</p>
//     *
//     * @param values an array of T[] objects
//     * @param consumer a {@link java.util.function.Consumer} object
//     * @param <T> a T class
//     */
//    public static <T> void forEach(T[] values, Consumer<T> consumer) {
//        forEach(values, (i, e) -> consumer.accept(e));
//    }
//
//    /**
//     * <p>anyNull.</p>
//     *
//     * @param values a {@link java.lang.Object} object
//     * @return a boolean
//     */
//    public static boolean anyNull(final Object... values) {
//        return !allNotNull(values);
//    }
//
//    /**
//     * <p>allNotNull.</p>
//     *
//     * @param values a {@link java.lang.Object} object
//     * @return a boolean
//     */
//    public static boolean allNotNull(final Object... values) {
//        return values != null && Stream.of(values).noneMatch(Objects::isNull);
//    }
//}
