package com.chensoul.jackson;

import java.util.Collection;

/**
 * This is {@link ComponentSerializers} that allows modules to register objects and classes
 * they wish to let the underlying engine serialize explicitly. Most specifically, this is used by
 * ticket registry backends such as Kryo that deal with serialized data before passing the object
 * onto memcached where explicit registration is required. The plan is passed to modules that do
 * have a need to register classes explicitly, and engines and libraries such as kryo can explicitly
 * ask the executing plan for all classes that they can handle and register in their own engine.
 *
 * @see <a href="https://github.com/apereo/cas/blob/master/api/cas-server-core-api-util/src/main/java/org/apereo/cas/util/serialization/ComponentSerializationPlan.java">ComponentSerializationPlan</a>
 */
public interface ComponentSerializers {

    /**
     * Register serializable class.
     *
     * @param clazz the clazz to register
     */
    void registerSerializableClass(Class clazz);

    /**
     * Register serializable class.
     *
     * @param clazz the clazz to register
     * @param order the order in which the class will be positioned in the registry of classes
     */
    void registerSerializableClass(Class clazz, Integer order);

    /**
     * Gets registered classes.
     *
     * @return the registered classes
     */
    Collection<Class> getRegisteredClasses();
}
