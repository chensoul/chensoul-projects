package com.chensoul.jackson.support;

import com.chensoul.jackson.ComponentSerializers;
import com.chensoul.lang.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * This is {@link DefaultComponentSerializers}.
 */
@Slf4j
public class DefaultComponentSerializers implements ComponentSerializers {

    private final List<Pair<Class, Integer>> registeredClasses = new ArrayList<>(0);

    @Override
    public void registerSerializableClass(final Class clazz) {
        registerSerializableClass(clazz, Integer.MAX_VALUE);
    }

    @Override
    public void registerSerializableClass(final Class clazz, final Integer order) {
        log.trace("Registering serializable class [{}] with order [{}]", clazz.getName(), order);
        this.registeredClasses.add(Pair.of(clazz, order));
    }

    @Override
    public Collection<Class> getRegisteredClasses() {
        return this.registeredClasses.stream()
            .sorted(Comparator.comparingInt(Pair::getRight))
            .map(Pair::getLeft)
                .collect(Collectors.toSet());
    }
}
