package com.chensoul.collection;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Unmodifiable {@link Iterator}
 *
 * @param <E> the type of elements returned by this iterator
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class UnmodifiableIterator<E> extends ReadOnlyIterator<E> {

    private final Iterator<E> delegate;

    public UnmodifiableIterator(Iterator<E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public E next() {
        return delegate.next();
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        delegate.forEachRemaining(action);
    }
}
