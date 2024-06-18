package com.chensoul.jpa.support;

import java.util.function.Supplier;
import javax.validation.groups.Default;

/**
 * @author zhijun.chen
 * @since 0.0.1
 */
public interface Create<T> extends Default {

    UpdateHandler<T> create(Supplier<T> supplier);

}
