package com.chensoul.reflect;

import com.chensoul.collection.ArrayUtils;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * Multiple {@link java.lang.reflect.Type}
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
public class MultipleType {

    private final Type[] types;

    private MultipleType(Type... types) {
        this.types = types;
    }

    /**
     * <p>of.</p>
     *
     * @param one a {@link java.lang.reflect.Type} object
     * @param two a {@link java.lang.reflect.Type} object
     * @return a {@link com.chensoul.reflect.MultipleType} object
     */
    public static MultipleType of(Type one, Type two) {
        return new MultipleType(one, two);
    }

    /**
     * <p>of.</p>
     *
     * @param one a {@link java.lang.reflect.Type} object
     * @param two a {@link java.lang.reflect.Type} object
     * @param others a {@link java.lang.reflect.Type} object
     * @return a {@link com.chensoul.reflect.MultipleType} object
     */
    public static MultipleType of(Type one, Type two, Type... others) {
        Type[] oneAndTwo = ArrayUtils.of(one, two);
        Type[] types = ArrayUtils.combineArray(oneAndTwo, others);
        return new MultipleType(types);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(types);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultipleType that = (MultipleType) o;
        return Arrays.equals(types, that.types);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "MultipleType : " + Arrays.toString(types);
    }
}
