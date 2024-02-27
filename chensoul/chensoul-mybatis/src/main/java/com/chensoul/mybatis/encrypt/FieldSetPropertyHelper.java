package com.chensoul.mybatis.encrypt;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.chensoul.mybatis.annotation.FieldBind;
import com.chensoul.mybatis.annotation.FieldEncrypt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.springframework.util.ReflectionUtils;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class FieldSetPropertyHelper {

    private static boolean hasFieldEncrypt = false;

    private static boolean hasFieldBind = false;

    private static Map<Class<?>, List<FieldSetProperty>> clazzMap;

    private static Set<Class<?>> excludeClazzSet;

    /**
     *
     */
    private FieldSetPropertyHelper() {
    }

    /**
     * @param var1
     * @param var2
     */
    public static void init(boolean var1, boolean var2) {
        hasFieldEncrypt = var1;
        hasFieldBind = var2;
        clazzMap = new ConcurrentHashMap<>();
        excludeClazzSet = new CopyOnWriteArraySet<>();
    }

    /**
     * @param clazz
     * @return
     */
    public static List<FieldSetProperty> getFieldSetProperties(Class<?> clazz) {
        if (excludeClazzSet.contains(clazz)) {
            return new ArrayList<>();
        }

        List<FieldSetProperty> fieldSetProperties = clazzMap.get(clazz);
        if (fieldSetProperties != null) {
            return fieldSetProperties;
        }

        if (clazz.isAssignableFrom(HashMap.class)) {
            excludeClazzSet.add(clazz);
        } else {
            List<FieldSetProperty> finalFieldSetPropertyList = new ArrayList<>();

            ReflectionUtils.doWithFields(clazz, field -> {
                FieldEncrypt fieldEncrypt = null;
                if (hasFieldEncrypt) {
                    fieldEncrypt = field.getAnnotation(FieldEncrypt.class);
                    if (null != fieldEncrypt && !field.getType().isAssignableFrom(String.class)) {
                        throw new RuntimeException("annotation `@FieldEncrypt` only string types are supported.");
                    }
                }

                FieldBind fieldBind = null;
                if (hasFieldBind) {
                    fieldBind = field.getAnnotation(FieldBind.class);
                }
                if (fieldBind != null || fieldEncrypt != null) {
                    finalFieldSetPropertyList.add(new FieldSetProperty(field.getName(), fieldEncrypt, fieldBind));
                }
            });

            fieldSetProperties = finalFieldSetPropertyList;

            if (fieldSetProperties.isEmpty()) {
                excludeClazzSet.add(clazz);
            } else {
                clazzMap.put(clazz, fieldSetProperties);
            }
        }

        return fieldSetProperties;
    }

    /**
     * @param configuration
     * @param value
     * @param consumer
     * @return
     */
    public static boolean foreachValue(Configuration configuration, Object value,
                                       BiConsumer<MetaObject, FieldSetProperty> consumer) {
        if (value == null) {
            return Boolean.FALSE;
        }
        List<FieldSetProperty> fieldSetPropertyList = getFieldSetProperties(value.getClass());
        if (!CollectionUtils.isEmpty(fieldSetPropertyList)) {
            MetaObject metaObject = configuration.newMetaObject(value);
            fieldSetPropertyList.parallelStream()
                    .forEach(fieldSetProperty -> consumer.accept(metaObject, fieldSetProperty));
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
