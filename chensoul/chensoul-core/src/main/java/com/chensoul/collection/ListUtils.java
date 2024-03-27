package com.chensoul.collection;


import java.util.ArrayList;
import static java.util.Collections.unmodifiableList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The utilities class for Java {@link java.util.List}
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @see List
 * @since 0.0.1
 *
 */
public abstract class ListUtils {
    public static boolean isList(Object values) {
        return values instanceof List;
    }

    /**
     * <p>toList.</p>
     *
     * @param iterable a {@link java.lang.Iterable} object
     * @param <E> a E class
     * @return a {@link java.util.List} object
     */
    public static <E> List<E> toList(Iterable<E> iterable) {
        return toList(iterable.iterator());
    }

    /**
     * <p>toList.</p>
     *
     * @param enumeration a {@link java.util.Enumeration} object
     * @param <E> a E class
     * @return a {@link java.util.List} object
     */
    public static <E> List<E> toList(Enumeration<E> enumeration) {
        return toList(CollectionUtils.toIterator(enumeration));
    }

    /**
     * <p>toList.</p>
     *
     * @param iterator a {@link java.util.Iterator} object
     * @param <E> a E class
     * @return a {@link java.util.List} object
     */
    public static <E> List<E> toList(Iterator<E> iterator) {
        List<E> list = newLinkedList();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    /**
     * <p>asList.</p>
     *
     * @param iterable a {@link java.lang.Iterable} object
     * @param <E> a E class
     * @return a {@link java.util.List} object
     */
    public static <E> List<E> asList(Iterable<E> iterable) {
        return asList(iterable.iterator());
    }

    /**
     * <p>asList.</p>
     *
     * @param enumeration a {@link java.util.Enumeration} object
     * @param <E> a E class
     * @return a {@link java.util.List} object
     */
    public static <E> List<E> asList(Enumeration<E> enumeration) {
        return asList(CollectionUtils.toIterator(enumeration));
    }

    /**
     * <p>asList.</p>
     *
     * @param iterator a {@link java.util.Iterator} object
     * @param <E> a E class
     * @return a {@link java.util.List} object
     */
    public static <E> List<E> asList(Iterator<E> iterator) {
        return unmodifiableList(toList(iterator));
    }

    /**
     * <p>newArrayList.</p>
     *
     * @param size a int
     * @param <E> a E class
     * @return a {@link java.util.ArrayList} object
     */
    public static <E> ArrayList<E> newArrayList(int size) {
        return new ArrayList<>(size);
    }

    /**
     * <p>newArrayList.</p>
     *
     * @param values a {@link java.lang.Iterable} object
     * @param <E> a E class
     * @return a {@link java.util.ArrayList} object
     */
    public static <E> ArrayList<E> newArrayList(Iterable<E> values) {
        ArrayList<E> list = newArrayList();
        for (E value : values) {
            list.add(value);
        }
        return list;
    }

    /**
     * <p>newArrayList.</p>
     *
     * @param <E> a E class
     * @return a {@link java.util.ArrayList} object
     */
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<>();
    }

    /**
     * <p>newLinkedList.</p>
     *
     * @param values a {@link java.lang.Iterable} object
     * @param <E> a E class
     * @return a {@link java.util.LinkedList} object
     */
    public static <E> LinkedList<E> newLinkedList(Iterable<E> values) {
        LinkedList<E> list = newLinkedList();
        for (E value : values) {
            list.add(value);
        }
        return list;
    }

    /**
     * <p>newLinkedList.</p>
     *
     * @param <E> a E class
     * @return a {@link java.util.LinkedList} object
     */
    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<>();
    }

    /**
     * <p>forEach.</p>
     *
     * @param values a {@link java.util.List} object
     * @param indexedElementConsumer a {@link java.util.function.BiConsumer} object
     * @param <T> a T class
     */
    public static <T> void forEach(List<T> values, BiConsumer<Integer, T> indexedElementConsumer) {
        int length = CollectionUtils.size(values);
        for (int i = 0; i < length; i++) {
            T value = values.get(i);
            indexedElementConsumer.accept(i, value);
        }
    }

    /**
     * <p>forEach.</p>
     *
     * @param values a {@link java.util.List} object
     * @param consumer a {@link java.util.function.Consumer} object
     * @param <T> a T class
     */
    public static <T> void forEach(List<T> values, Consumer<T> consumer) {
        forEach(values, (i, e) -> consumer.accept(e));
    }
}
