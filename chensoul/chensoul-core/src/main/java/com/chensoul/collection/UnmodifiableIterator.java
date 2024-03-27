package com.chensoul.collection;

import java.util.Iterator;
import java.util.function.Consumer;
public class UnmodifiableIterator<E> extends ReadOnlyIterator<E> {

    private final Iterator<E> delegate;
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
