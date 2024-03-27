package com.chensoul.collection;

import java.util.Iterator;
public abstract class ReadOnlyIterator<E> implements Iterator<E> {
    @Override
    public final void remove() {
        throw new UnsupportedOperationException("Read-Only");
    }

}
