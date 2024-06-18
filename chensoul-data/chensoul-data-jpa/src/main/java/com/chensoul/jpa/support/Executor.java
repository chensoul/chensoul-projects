package com.chensoul.jpa.support;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author zhijun.chen
 * @since 0.0.1
 */
public interface Executor<T> {

	Optional<T> execute();

	Executor<T> onSuccess(Consumer<T> consumer);

	Executor<T> onError(Consumer<? super Throwable> consumer);
}
