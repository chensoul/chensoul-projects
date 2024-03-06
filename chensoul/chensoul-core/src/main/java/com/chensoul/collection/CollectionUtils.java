package com.chensoul.collection;

import static com.chensoul.collection.ArrayUtils.length;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * The utilities class for Java Collection
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @version 1.0.0
 * @see Collections
 */
public abstract class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static <E> Iterable<E> toIterable(Collection<E> collection) {
        return collection;
    }

    public static <E> Iterable<E> toIterable(Iterator<E> iterator) {
        return new IterableAdapter(iterator);
    }

    public static <E> Iterator<E> toIterator(Enumeration<E> enumeration) {
        return new EnumerationIterator(enumeration);
    }

    public static <E> Iterable<E> toIterable(Enumeration<E> enumeration) {
        return toIterable(toIterator(enumeration));
    }

    public static <E> Iterator<E> unmodifiableIterator(Iterator<E> iterator) {
        return new UnmodifiableIterator(iterator);
    }

    /**
     * Get the size of the specified {@link Collection}
     *
     * @param collection the specified {@link Collection}
     * @return must be positive number
     */
    public static int size(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    /**
     * Get the size of the specified {@link Iterable}
     *
     * @param iterable the specified {@link Iterable}
     * @return must be positive number
     */
    public static int size(Iterable<?> iterable) {
        if (iterable instanceof Collection) {
            return size((Collection) iterable);
        }
        Iterator<?> iterator = iterable.iterator();
        int size = 0;
        while (iterator.hasNext()) {
            iterator.next();
            size++;
        }
        return size;
    }

    /**
     * Compares the specified collection with another, the main implementation references
     * {@link AbstractSet}
     *
     * @param one     {@link Collection}
     * @param another {@link Collection}
     * @return if equals, return <code>true</code>, or <code>false</code>
     */
    public static boolean equals(Collection<?> one, Collection<?> another) {

        if (one == another) {
            return true;
        }

        if (isEmpty(one) && isEmpty(another)) {
            return true;
        }

        if (size(one) != size(another)) {
            return false;
        }

        try {
            return one.containsAll(another);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    /**
     * Add the multiple values into {@link Collection the specified collection}
     *
     * @param collection {@link Collection the specified collection}
     * @param values     the multiple values
     * @param <T>        the type of values
     * @return the effected count after added
     */
    public static <T> int addAll(Collection<T> collection, T... values) {

        int size = length(values);

        if (size < 1) {
            return 0;
        }

        if (collection == null) {
            return 0;
        }

        int effectedCount = 0;
        for (int i = 0; i < size; i++) {
            if (collection.add(values[i])) {
                effectedCount++;
            }
        }

        return effectedCount;
    }

    /**
     * Take the first element from the specified collection
     *
     * @param values the collection object
     * @param <T>    the type of element of collection
     * @return if found, return the first one, or <code>null</code>
     */
    public static <T> T first(Collection<T> values) {
        if (isEmpty(values)) {
            return null;
        }
        if (values instanceof List) {
            List<T> list = (List<T>) values;
            return list.get(0);
        } else {
            return first((Iterable<T>) values);
        }
    }

    /**
     * Take the first element from the specified {@link Iterable}
     *
     * @param values the {@link Iterable} object
     * @param <T>    the type of element of {@link Iterable}
     * @return if found, return the first one, or <code>null</code>
     */
    public static <T> T first(Iterable<T> values) {
        return values == null ? null : first(values.iterator());
    }

    /**
     * Take the first element from the specified {@lin Iterator}
     *
     * @param values the {@link Iterator} object
     * @param <T>    the type of element of {@lin Iterator}
     * @return if found, return the first one, or <code>null</code>
     */
    public static <T> T first(Iterator<T> values) {
        if (values == null || !values.hasNext()) {
            return null;
        }
        return values.next();
    }
}
