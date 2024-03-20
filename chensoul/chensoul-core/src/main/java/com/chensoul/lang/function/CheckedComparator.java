package com.chensoul.lang.function;

/*-
 * #%L
 * Chensoul :: Core
 * %%
 * Copyright (C) 2023 - 2024 chensoul.cc
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * A {@link java.util.Comparator}-like that allows for checked exceptions.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
@FunctionalInterface
public interface CheckedComparator<T> {

    /**
     * Compares its two arguments for order.
     *
     * @param o1 a T object
     * @param o2 a T object
     * @return a int
     * @throws java.lang.Throwable if any.
     */
    int compare(T o1, T o2) throws Throwable;

    /**
     * <p>sneaky.</p>
     *
     * @param callable a {@link com.chensoul.lang.function.CheckedComparator} object
     * @param <T> a T class
     * @return a {@link java.util.Comparator} object
     */
    static <T> Comparator<T> sneaky(CheckedComparator<T> callable) {
        return unchecked(callable, FunctionUtils.SNEAKY_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param callable a {@link com.chensoul.lang.function.CheckedComparator} object
     * @param <T> a T class
     * @return a {@link java.util.Comparator} object
     */
    static <T> Comparator<T> unchecked(CheckedComparator<T> callable) {
        return unchecked(callable, FunctionUtils.CHECKED_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param callable a {@link com.chensoul.lang.function.CheckedComparator} object
     * @param handler a {@link java.util.function.Consumer} object
     * @param <T> a T class
     * @return a {@link java.util.Comparator} object
     */
    static <T> Comparator<T> unchecked(CheckedComparator<T> callable, Consumer<Throwable> handler) {
        return (t1, t2) -> {
            try {
                return callable.compare(t1, t2);
            } catch (Throwable e) {
                handler.accept(e);
                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }
}
