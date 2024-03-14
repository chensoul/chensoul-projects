package com.chensoul.lang.function;

import static com.chensoul.lang.function.Predicates.and;
import static com.chensoul.lang.function.Predicates.or;
import static java.util.stream.Collectors.toList;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <p>Streams interface.</p>
 *
 * @author chensoul
 * @version $Id: $Id
 */
public interface Streams {

    /**
     * <p>stream.</p>
     *
     * @param iterable a {@link java.lang.Iterable} object
     * @param <T> a T class
     * @return a {@link java.util.stream.Stream} object
     */
    static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * <p>filterStream.</p>
     *
     * @param values a S object
     * @param predicate a {@link java.util.function.Predicate} object
     * @param <T> a T class
     * @param <S> a S class
     * @return a {@link java.util.stream.Stream} object
     */
    static <T, S extends Iterable<T>> Stream<T> filterStream(S values, Predicate<? super T> predicate) {
        return StreamSupport.stream(values.spliterator(), false).filter(predicate);
    }

    /**
     * <p>filterList.</p>
     *
     * @param values a S object
     * @param predicate a {@link java.util.function.Predicate} object
     * @param <T> a T class
     * @param <S> a S class
     * @return a {@link java.util.List} object
     */
    static <T, S extends Iterable<T>> List<T> filterList(S values, Predicate<? super T> predicate) {
        return filterStream(values, predicate).collect(toList());
    }

    /**
     * <p>filterSet.</p>
     *
     * @param values a S object
     * @param predicate a {@link java.util.function.Predicate} object
     * @param <T> a T class
     * @param <S> a S class
     * @return a {@link java.util.Set} object
     */
    static <T, S extends Iterable<T>> Set<T> filterSet(S values, Predicate<? super T> predicate) {
        // new Set with insertion order
        return filterStream(values, predicate).collect(LinkedHashSet::new, Set::add, Set::addAll);
    }

    /**
     * <p>filter.</p>
     *
     * @param values a S object
     * @param predicate a {@link java.util.function.Predicate} object
     * @param <T> a T class
     * @param <S> a S class
     * @return a S object
     */
    static <T, S extends Iterable<T>> S filter(S values, Predicate<? super T> predicate) {
        final boolean isSet = Set.class.isAssignableFrom(values.getClass());
        return (S) (isSet ? filterSet(values, predicate) : filterList(values, predicate));
    }

    /**
     * <p>filterAll.</p>
     *
     * @param values a S object
     * @param predicates a {@link java.util.function.Predicate} object
     * @param <T> a T class
     * @param <S> a S class
     * @return a S object
     */
    static <T, S extends Iterable<T>> S filterAll(S values, Predicate<? super T>... predicates) {
        return filter(values, and(predicates));
    }

    /**
     * <p>filterAny.</p>
     *
     * @param values a S object
     * @param predicates a {@link java.util.function.Predicate} object
     * @param <T> a T class
     * @param <S> a S class
     * @return a S object
     */
    static <T, S extends Iterable<T>> S filterAny(S values, Predicate<? super T>... predicates) {
        return filter(values, or(predicates));
    }

    /**
     * <p>filterFirst.</p>
     *
     * @param values a {@link java.lang.Iterable} object
     * @param predicates a {@link java.util.function.Predicate} object
     * @param <T> a T class
     * @return a T object
     */
    static <T> T filterFirst(Iterable<T> values, Predicate<? super T>... predicates) {
        return StreamSupport.stream(values.spliterator(), false)
            .filter(and(predicates))
            .findFirst()
            .orElse(null);
    }
}
