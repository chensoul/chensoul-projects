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

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * A {@link Comparator}-like that allows for checked exceptions.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@FunctionalInterface
public interface CheckedComparator<T> {

    /**
     * Compares its two arguments for order.
     */
    int compare(T o1, T o2) throws Throwable;

    static <T> Comparator<T> sneaky(CheckedComparator<T> callable) {
        return unchecked(callable, FunctionUtils.RETHROW_ALL);
    }

    static <T> Comparator<T> unchecked(CheckedComparator<T> callable) {
        return unchecked(callable, FunctionUtils.THROWABLE_TO_RUNTIME_EXCEPTION);
    }

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
