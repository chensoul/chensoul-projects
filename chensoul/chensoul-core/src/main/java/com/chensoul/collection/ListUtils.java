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
 * The utilities class for Java {@link List}
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @see List
 * @since 0.0.1
 */
public abstract class ListUtils {

    public static boolean isList(Object values) {
        return values instanceof List;
    }

    public static <E> List<E> toList(Iterable<E> iterable) {
        return toList(iterable.iterator());
    }

    public static <E> List<E> toList(Enumeration<E> enumeration) {
        return toList(CollectionUtils.toIterator(enumeration));
    }

    public static <E> List<E> toList(Iterator<E> iterator) {
        List<E> list = newLinkedList();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static <E> List<E> asList(Iterable<E> iterable) {
        return asList(iterable.iterator());
    }

    public static <E> List<E> asList(Enumeration<E> enumeration) {
        return asList(CollectionUtils.toIterator(enumeration));
    }

    public static <E> List<E> asList(Iterator<E> iterator) {
        return unmodifiableList(toList(iterator));
    }

    public static <E> ArrayList<E> newArrayList(int size) {
        return new ArrayList<>(size);
    }

    public static <E> ArrayList<E> newArrayList(Iterable<E> values) {
        ArrayList<E> list = newArrayList();
        for (E value : values) {
            list.add(value);
        }
        return list;
    }

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<>();
    }

    public static <E> LinkedList<E> newLinkedList(Iterable<E> values) {
        LinkedList<E> list = newLinkedList();
        for (E value : values) {
            list.add(value);
        }
        return list;
    }

    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<>();
    }

    public static <T> void forEach(List<T> values, BiConsumer<Integer, T> indexedElementConsumer) {
        int length = CollectionUtils.size(values);
        for (int i = 0; i < length; i++) {
            T value = values.get(i);
            indexedElementConsumer.accept(i, value);
        }
    }

    public static <T> void forEach(List<T> values, Consumer<T> consumer) {
        forEach(values, (i, e) -> consumer.accept(e));
    }
}
