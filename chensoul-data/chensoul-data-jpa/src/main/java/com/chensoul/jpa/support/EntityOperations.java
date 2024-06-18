package com.chensoul.jpa.support;

import com.chensoul.jpa.support.EntityCreator;
import org.springframework.data.repository.CrudRepository;

/**
 * @author zhijun.chen
 * @since 0.0.1
 */
@SuppressWarnings("unchecked")
public abstract class EntityOperations {

    public static <T, ID> EntityUpdater<T, ID> doUpdate(CrudRepository<T, ID> repository) {
        return new EntityUpdater<>(repository);
    }

    public static <T, ID> EntityCreator<T, ID> doCreate(CrudRepository<T, ID> repository) {
        return new EntityCreator(repository);
    }


}
