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

import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * A {@link java.util.function.BiPredicate} that allows for checked exceptions.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
@FunctionalInterface
public interface CheckedBiPredicate<T, U> {

    /**
     * <p>test.</p>
     *
     * @param t a T object
     * @param u a U object
     * @return a boolean
     * @throws java.lang.Throwable if any.
     */
    boolean test(T t, U u) throws Throwable;

    /**
     * <p>sneaky.</p>
     *
     * @param predicate a {@link com.chensoul.lang.function.CheckedBiPredicate} object
     * @param <T> a T class
     * @param <U> a U class
     * @return a {@link java.util.function.BiPredicate} object
     */
    static <T, U> BiPredicate<T, U> sneaky(CheckedBiPredicate<T, U> predicate) {
        return unchecked(predicate, FunctionUtils.SNEAKY_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param predicate a {@link com.chensoul.lang.function.CheckedBiPredicate} object
     * @param <T> a T class
     * @param <U> a U class
     * @return a {@link java.util.function.BiPredicate} object
     */
    static <T, U> BiPredicate<T, U> unchecked(CheckedBiPredicate<T, U> predicate) {
        return unchecked(predicate, FunctionUtils.CHECKED_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param predicate a {@link com.chensoul.lang.function.CheckedBiPredicate} object
     * @param handler a {@link java.util.function.Consumer} object
     * @param <T> a T class
     * @param <U> a U class
     * @return a {@link java.util.function.BiPredicate} object
     */
    static <T, U> BiPredicate<T, U> unchecked(CheckedBiPredicate<T, U> predicate, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                return predicate.test(t, u);
            } catch (Throwable e) {
                handler.accept(e);
                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }
}
