package com.chensoul.collection;

import static java.util.Collections.emptyEnumeration;
import java.util.Enumeration;

/**
 * {@link java.util.Iterator} Adapter based on {@link java.util.Enumeration}
 *
 * @param <E> The elements' type
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 *
 */
public class EnumerationIterator<E> extends ReadOnlyIterator<E> {

    private final Enumeration<E> enumeration;

    EnumerationIterator(Enumeration<E> enumeration) {
        this.enumeration = enumeration == null ? emptyEnumeration() : enumeration;
    }
    @Override
    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    /** {@inheritDoc} */
    @Override
    public E next() {
        return enumeration.nextElement();
    }
}
