package com.chensoul.collection;

import static com.chensoul.collection.ArrayUtils.length;
import static com.chensoul.collection.CollectionUtils.size;
import java.util.Collection;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.unmodifiableSet;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The utilities class for Java {@link Set}
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @see Set
 * @since 0.0.1
 */
public abstract class SetUtils {

    public static boolean isSet(Iterable<?> elements) {
        return elements instanceof Set;
    }

    /**
     * Convert to multiple elements to be {@link LinkedHashSet}
     *
     * @param elements one or more elements
     * @param <E>      the type of <code>elements</code>
     * @return read-only {@link Set}
     */
    public static <E> Set<E> of(E... elements) {
        int size = length(elements);
        if (size < 1) {
            return emptySet();
        } else if (size == 1) {
            return singleton(elements[0]);
        }

        Set<E> set = new LinkedHashSet<>(size);

        for (int i = 0; i < size; i++) {
            set.add(elements[i]);
        }
        return unmodifiableSet(set);
    }

    /**
     * Build a read-only {@link Set} from the given {@lin Enumeration} elements
     *
     * @param elements one or more elements
     * @param <E>      the type of <code>elements</code>
     * @return non-null read-only {@link Set}
     */
    public static <E> Set<E> asSet(Enumeration<E> elements) {
        return asSet(CollectionUtils.toIterable(elements));
    }

    /**
     * Convert to multiple elements to be {@link LinkedHashSet}
     *
     * @param elements one or more elements
     * @param <E>      the type of <code>elements</code>
     * @return read-only {@link Set}
     */
    public static <E> Set<E> asSet(Iterable<E> elements) {
        return unmodifiableSet(newLinkedHashSet(elements));
    }

    /**
     * Convert to one or more elements to be a read-only {@link Set}
     *
     * @param one    one element
     * @param others others elements
     * @param <E>    the type of <code>elements</code>
     * @return read-only {@link Set}
     */
    public static <E> Set<E> asSet(E one, E... others) {
        int othersSize = length(others);
        if (othersSize < 1) {
            return singleton(one);
        }

        Set<E> elements = new LinkedHashSet<>(othersSize + 1, MapUtils.MIN_LOAD_FACTOR);

        elements.add(one);

        for (int i = 0; i < othersSize; i++) {
            elements.add(others[i]);
        }

        return unmodifiableSet(elements);
    }

    public static <T> Set<T> asSet(Collection<T> elements, T... others) {
        int valuesSize = size(elements);

        if (valuesSize < 1) {
            return of(others);
        }

        int othersSize = length(others);

        if (othersSize < 1) {
            return asSet(elements);
        }

        int size = valuesSize + othersSize;

        Set<T> set = newLinkedHashSet(size, MapUtils.MIN_LOAD_FACTOR);
        // add elements
        set.addAll(elements);

        // add others
        for (T other : others) {
            set.add(other);
        }
        return unmodifiableSet(set);
    }

    public static <E> Set<E> newHashSet(Iterable<E> elements) {
        Set<E> set = newHashSet();
        for (E value : elements) {
            set.add(value);
        }
        return set;
    }

    public static <E> Set<E> newHashSet(Collection<? extends E> elements) {
        return new HashSet(elements);
    }

    public static <E> Set<E> newHashSet() {
        return new HashSet<>();
    }

    public static <E> Set<E> newHashSet(int initialCapacity) {
        return new HashSet<>(initialCapacity);
    }

    public static <E> Set<E> newHashSet(int initialCapacity, float loadFactor) {
        return new HashSet<>(initialCapacity, loadFactor);
    }

    public static <E> Set<E> newLinkedHashSet(Iterable<E> elements) {
        return newLinkedHashSet(elements.iterator());
    }

    public static <E> Set<E> newLinkedHashSet(Iterator<E> elements) {
        Set<E> set = newLinkedHashSet();
        while (elements.hasNext()) {
            E value = elements.next();
            set.add(value);
        }
        return set;
    }

    public static <E> Set<E> newLinkedHashSet(Collection<? extends E> elements) {
        return new LinkedHashSet(elements);
    }

    public static <E> Set<E> newLinkedHashSet() {
        return new LinkedHashSet<>();
    }

    public static <E> Set<E> newLinkedHashSet(int initialCapacity) {
        return new LinkedHashSet<>(initialCapacity);
    }

    public static <E> Set<E> newLinkedHashSet(int initialCapacity, float loadFactor) {
        return new LinkedHashSet<>(initialCapacity, loadFactor);
    }
}
