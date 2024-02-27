package com.chensoul.jackson;

/**
 * This is {@link ComponentSerializersConfigurer}, to be implemented
 * by modules that wish to register serializable classes into the plan.
 */
@FunctionalInterface
public interface ComponentSerializersConfigurer {
    /**
     * configure the plan.
     *
     * @param plan the plan
     */
    void configure(ComponentSerializers plan);

    /**
     * Gets name.
     *
     * @return the name
     */
    default String getName() {
        return getClass().getSimpleName();
    }
}
