/**
 * Copyright (c), Data Geekery GmbH, contact@datageekery.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chensoul.lang.function;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A {@link java.util.function.BiConsumer} that allows for checked exceptions.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
@FunctionalInterface
public interface CheckedBiConsumer<T, U> {

    /**
     * <p>accept.</p>
     *
     * @param t a T object
     * @param u a U object
     * @throws java.lang.Throwable if any.
     */
    void accept(T t, U u) throws Throwable;

    /**
     * <p>sneaky.</p>
     *
     * @param consumer a {@link com.chensoul.lang.function.CheckedBiConsumer} object
     * @param <T> a T class
     * @param <U> a U class
     * @return a {@link java.util.function.BiConsumer} object
     */
    static <T, U> BiConsumer<T, U> sneaky(final CheckedBiConsumer<T, U> consumer) {
        return unchecked(consumer, FunctionUtils.SNEAKY_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param consumer a {@link com.chensoul.lang.function.CheckedBiConsumer} object
     * @param <T> a T class
     * @param <U> a U class
     * @return a {@link java.util.function.BiConsumer} object
     */
    static <T, U> BiConsumer<T, U> unchecked(final CheckedBiConsumer<T, U> consumer) {
        return unchecked(consumer, FunctionUtils.CHECKED_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param consumer a {@link com.chensoul.lang.function.CheckedBiConsumer} object
     * @param handler a {@link java.util.function.Consumer} object
     * @param <T> a T class
     * @param <U> a U class
     * @return a {@link java.util.function.BiConsumer} object
     */
    static <T, U> BiConsumer<T, U> unchecked(final CheckedBiConsumer<T, U> consumer, final Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                consumer.accept(t, u);
            } catch (final Throwable e) {
                handler.accept(e);
                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }
}
