package com.chensoul.jpa.support;

import java.util.function.Supplier;

/**
 * @author zhijun.chen
 * @since 0.0.1
 */
public interface Loader<T, ID> {

	UpdateHandler<T> loadById(ID id);

	UpdateHandler<T> load(Supplier<T> t);

}
