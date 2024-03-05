package com.chensoul.reflect;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * {@link Type} Argument
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class TypeArgument {

    private final Type type;

    private final int index;

    protected TypeArgument(Type type, int index) {
        this.type = type;
        this.index = index;
    }

    public static TypeArgument create(Type type, int index) {
        return new TypeArgument(type, index);
    }

    @Override
    public String toString() {
        return "TypeArgument{" +
               "type=" + type +
               ", index=" + index +
               '}';
    }

    public Type getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeArgument that = (TypeArgument) o;

        if (index != that.index) return false;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + index;
        return result;
    }
}
