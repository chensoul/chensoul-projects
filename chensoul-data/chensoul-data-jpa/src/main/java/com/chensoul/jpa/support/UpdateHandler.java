package com.chensoul.jpa.support;

import com.chensoul.jpa.support.Executor;
import java.util.function.Consumer;

/**
 * @author zhijun.chen
 * @since 0.0.1
 */
public interface UpdateHandler<T> {

    Executor<T> update(Consumer<T> consumer);

}
