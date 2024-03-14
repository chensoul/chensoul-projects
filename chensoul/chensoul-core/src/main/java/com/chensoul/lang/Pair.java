package com.chensoul.lang;

import lombok.Data;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
@Data
public class Pair<L, R> {
    private L left;
    private R right;

    /**
     * <p>Constructor for Pair.</p>
     *
     * @param left a L object
     * @param right a R object
     */
    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * <p>of.</p>
     *
     * @param left a L object
     * @param right a R object
     * @param <L> a L class
     * @param <R> a R class
     * @return a {@link com.chensoul.lang.Pair} object
     */
    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }
}
