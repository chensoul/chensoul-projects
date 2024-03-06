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

import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * A {@link BiPredicate} that allows for checked exceptions.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@FunctionalInterface
public interface CheckedBiPredicate<T, U> {

    boolean test(T t, U u) throws Throwable;

    static <T, U> BiPredicate<T, U> sneaky(CheckedBiPredicate<T, U> predicate) {
        return unchecked(predicate, FunctionUtils.RETHROW_ALL);
    }

    static <T, U> BiPredicate<T, U> unchecked(CheckedBiPredicate<T, U> predicate) {
        return unchecked(predicate, FunctionUtils.THROWABLE_TO_RUNTIME_EXCEPTION);
    }

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