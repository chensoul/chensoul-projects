package com.chensoul.collection;

import java.util.Iterator;

/**
 * {@link java.lang.Iterable} Adapter via {@link java.util.Iterator}
 *
 * @param <T> the type of elements returned by the iterator
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 *
 */
public class IterableAdapter<T> implements Iterable<T> {

    private final Iterator<T> iterator;
    public IterableAdapter(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<T> iterator() {
        return iterator;
    }
}
