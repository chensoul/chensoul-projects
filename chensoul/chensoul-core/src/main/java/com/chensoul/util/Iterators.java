package com.chensoul.util;

import java.util.Iterator;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class Iterators {

    private Iterators() {
    }

    public static <T> T getFirst(final Iterable<T> iterable) {
        if (null == iterable) {
            return null;
        }
        return getFirst(iterable.iterator());
    }

    public static <T> T getFirstNoneNull(final Iterable<T> iterable) {
        if (null == iterable) {
            return null;
        }
        return getFirstNoneNull(iterable.iterator());
    }

    public static <T> T getFirst(final Iterator<T> iterator) {
        if (null != iterator && iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    public static <T> T getFirstNoneNull(final Iterator<T> iterator) {
        if (null != iterator) {
            while (iterator.hasNext()) {
                final T next = iterator.next();
                if (null != next) {
                    return next;
                }
            }
        }
        return null;
    }


    /**
     * @param iterable
     * @return
     */
    public static boolean isEmpty(final Iterable<?> iterable) {
        return null == iterable || isEmpty(iterable.iterator());
    }

    /**
     * @param iterator
     * @return
     */
    public static boolean isEmpty(final Iterator<?> iterator) {
        return null == iterator || !iterator.hasNext();
    }

    /**
     * @param iterable
     * @return
     */
    public static boolean isNotEmpty(final Iterable<?> iterable) {
        return null != iterable && isNotEmpty(iterable.iterator());
    }

    /**
     * @param iterator
     * @return
     */
    public static boolean isNotEmpty(final Iterator<?> iterator) {
        return null != iterator && iterator.hasNext();
    }
}
