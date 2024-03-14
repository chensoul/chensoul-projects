package com.chensoul.collection;

import java.util.Iterator;

/**
 * Abstract Read-Only {@link java.util.Iterator}
 *
 * @param <E> the type of elements returned by this iterator
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
public abstract class ReadOnlyIterator<E> implements Iterator<E> {

    /** {@inheritDoc} */
    @Override
    public final void remove() {
        throw new UnsupportedOperationException("Read-Only");
    }

}
