package com.chensoul.reflect;

import com.chensoul.collection.ArrayUtils;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * Multiple {@link Type}
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class MultipleType {

    private final Type[] types;

    private MultipleType(Type... types) {
        this.types = types;
    }

    public static MultipleType of(Type one, Type two) {
        return new MultipleType(one, two);
    }

    public static MultipleType of(Type one, Type two, Type... others) {
        Type[] oneAndTwo = ArrayUtils.of(one, two);
        Type[] types = ArrayUtils.combineArray(oneAndTwo, others);
        return new MultipleType(types);
    }

    @Override
    public int hashCode() {
        return Objects.hash(types);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultipleType that = (MultipleType) o;
        return Arrays.equals(types, that.types);
    }

    @Override
    public String toString() {
        return "MultipleType : " + Arrays.toString(types);
    }
}
