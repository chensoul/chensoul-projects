package com.chensoul.reflect;

import java.lang.reflect.Type;
import java.util.Objects;
public class TypeArgument {

    private final Type type;

    private final int index;
    protected TypeArgument(Type type, int index) {
        this.type = type;
        this.index = index;
    }

    /**
     * <p>create.</p>
     *
     * @param type a {@link java.lang.reflect.Type} object
     * @param index a int
     * @return a {@link com.chensoul.reflect.TypeArgument} object
     */
    public static TypeArgument create(Type type, int index) {
        return new TypeArgument(type, index);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "TypeArgument{" +
            "type=" + type +
            ", index=" + index +
            '}';
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link java.lang.reflect.Type} object
     */
    public Type getType() {
        return type;
    }

    /**
     * <p>Getter for the field <code>index</code>.</p>
     *
     * @return a int
     */
    public int getIndex() {
        return index;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeArgument that = (TypeArgument) o;

        if (index != that.index) return false;
        return Objects.equals(type, that.type);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + index;
        return result;
    }
}
