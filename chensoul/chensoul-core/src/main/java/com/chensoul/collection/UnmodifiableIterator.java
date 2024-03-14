package com.chensoul.collection;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Unmodifiable {@link java.util.Iterator}
 *
 * @param <E> the type of elements returned by this iterator
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
public class UnmodifiableIterator<E> extends ReadOnlyIterator<E> {

    private final Iterator<E> delegate;

    /**
     * <p>Constructor for UnmodifiableIterator.</p>
     *
     * @param delegate a {@link java.util.Iterator} object
     */
    public UnmodifiableIterator(Iterator<E> delegate) {
        this.delegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    /** {@inheritDoc} */
    @Override
    public E next() {
        return delegate.next();
    }

    /** {@inheritDoc} */
    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        delegate.forEachRemaining(action);
    }
}
