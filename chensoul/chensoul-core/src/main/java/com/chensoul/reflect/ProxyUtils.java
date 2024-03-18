package com.chensoul.reflect;

/*-
 * #%L
 * Chensoul :: Core
 * %%
 * Copyright (C) 2023 - 2024 chensoul.cc
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static com.chensoul.collection.ArrayUtils.isArray;
import static com.chensoul.lang.function.Predicates.and;
import static com.chensoul.reflect.ClassUtils.isPrimitive;
import static com.chensoul.reflect.MemberUtils.FINAL_METHOD_PREDICATE;
import static com.chensoul.reflect.MemberUtils.NON_PRIVATE_METHOD_PREDICATE;
import static com.chensoul.reflect.MemberUtils.NON_STATIC_METHOD_PREDICATE;
import static com.chensoul.reflect.MethodUtils.OBJECT_METHOD_PREDICATE;
import java.lang.reflect.Method;
import static java.lang.reflect.Modifier.isFinal;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.function.Predicate;


/**
 * The utilities class for {@link Proxy Proxy}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 * @version $Id: $Id
 */
public abstract class ProxyUtils {

    /**
     * <ul>
     *     <li>class has a non-private constructor with no parameters</li>
     *     <li>class is not declared final</li>
     *     <li>class does not have non-static, final methods with public, protected or default visibility</li>
     *     <li>class is not primitive type</li>
     *     <li>class is not array type</li>
     * </ul>
     *
     * @param type a {@link java.lang.Class} object
     * @return a boolean
     */
    public static boolean isProxyable(Class<?> type) {
        if (isArray(type)) {
            return false;
        }

        if (isPrimitive(type)) {
            return false;
        }

        if (isFinal(type.getModifiers())) {
            return false;
        }

        if (!ConstructorUtils.hasNonPrivateConstructorWithoutParameters(type)) {
            return false;
        }

        Predicate<? super Method> predicate = and(NON_STATIC_METHOD_PREDICATE, FINAL_METHOD_PREDICATE,
            NON_PRIVATE_METHOD_PREDICATE, OBJECT_METHOD_PREDICATE.negate());

        List<Method> methods = MethodUtils.getAllDeclaredMethods(type, predicate);

        if (!methods.isEmpty()) {
            return false;
        }

        return true;
    }
}
