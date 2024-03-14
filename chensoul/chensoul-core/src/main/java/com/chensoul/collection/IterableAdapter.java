package com.chensoul.collection;

import java.util.Iterator;

/**
 * {@link java.lang.Iterable} Adapter via {@link java.util.Iterator}
 *
 * @param <T> the type of elements returned by the iterator
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
public class IterableAdapter<T> implements Iterable<T> {

    private final Iterator<T> iterator;

    /**
     * <p>Constructor for IterableAdapter.</p>
     *
     * @param iterator a {@link java.util.Iterator} object
     */
    public IterableAdapter(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<T> iterator() {
        return iterator;
    }
}
