package com.chensoul.mybatis.encrypt;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chensoul.mybatis.annotation.FieldEncrypt;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@Slf4j
public class InterceptorHelper {
    private InterceptorHelper() {
    }

    private static Map<Class<? extends Encryptor>, Encryptor> encryptorMap;

    /**
     * @param invocation
     * @param algorithm
     * @param password
     * @param encryptor
     * @return
     * @throws Throwable
     */
    public static Object encrypt(Invocation invocation, Algorithm algorithm, String password, Encryptor encryptor)
            throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];

        if (parameter == null) {
            return invocation.proceed();
        }

        SqlCommandType sqlCommandType = ms.getSqlCommandType();
        if (SqlCommandType.UNKNOWN == sqlCommandType || SqlCommandType.FLUSH == sqlCommandType) {
            return invocation.proceed();
        }

        encryptParams(ms.getConfiguration(), encryptor, algorithm, password, parameter);

        return invocation.proceed();
    }

    private static void encryptParams(Configuration configuration, Encryptor encryptor, Algorithm algorithm, String password, Object parameter) throws InvocationTargetException, IllegalAccessException {
        if (parameter instanceof Map) {
            Map<String, Object> paramMap = (Map<String, Object>) parameter;

            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                if (entry.getValue() == null || entry.getKey().startsWith("param")) {
                    continue;
                }

                if (entry.getValue() instanceof ArrayList) {
                    for (Object var : (ArrayList) entry.getValue()) {
                        encryptParam(configuration, encryptor, algorithm, password, var);
                    }
                } else if (entry.getValue() instanceof QueryWrapper) {
                    Object entity = ((QueryWrapper) entry.getValue()).getEntity();
                    if (entity == null) {
                        continue;
                    }
                    encryptParam(configuration, encryptor, algorithm, password, entity);
                } else {
                    encryptParam(configuration, encryptor, algorithm, password, entry.getValue());
                }
            }
        } else {
            encryptParam(configuration, encryptor, algorithm, password, parameter);
        }
    }

    /**
     * @param configuration
     * @param encryptor
     * @param algorithm
     * @param password
     * @param param
     * @return
     */
    public static boolean encryptParam(Configuration configuration, Encryptor encryptor, Algorithm algorithm, String password, Object param) {
        return FieldSetPropertyHelper.foreachValue(configuration, param, (metaObject, fieldSetProperty) -> {
            FieldEncrypt fieldEncrypt = fieldSetProperty.getFieldEncrypt();
            if (null == fieldEncrypt) {
                return;
            }
            Object objectValue = metaObject.getValue(fieldSetProperty.getFieldName());
            if (null == objectValue) {
                return;
            }

            try {
                String value = getEncryptor(encryptor, fieldEncrypt.encryptor()).encrypt(algorithm, password, (String) objectValue, null);
                metaObject.setValue(fieldSetProperty.getFieldName(), value);
            } catch (Exception e) {
                log.error("field encrypt", e.getMessage());
            }
        });
    }

    /**
     * @param encryptor
     * @param customEncryptor
     * @return
     */
    public static Encryptor getEncryptor(Encryptor encryptor, Class<? extends Encryptor> customEncryptor) {
        Encryptor result = encryptor;
        if (Encryptor.class == customEncryptor) {
            return result;
        }

        if (null == encryptorMap) {
            encryptorMap = new HashMap();
        }

        result = encryptorMap.get(customEncryptor);
        if (null == result) {
            try {
                result = customEncryptor.newInstance();
                encryptorMap.put(customEncryptor, result);
            } catch (Exception var4) {
                log.error("fieldEncrypt encryptor newInstance error", var4);
            }
        }
        return result;
    }

    /**
     * @param invocation
     * @param consumer
     * @return
     * @throws Throwable
     */
    public static Object decrypt(Invocation invocation, BiConsumer<MetaObject, FieldSetProperty> consumer)
            throws Throwable {
        List result = (List) invocation.proceed();
        if (result.isEmpty()) {
            return result;
        }

        DefaultResultSetHandler defaultResultSetHandler = (DefaultResultSetHandler) invocation.getTarget();
        Field field = defaultResultSetHandler.getClass().getDeclaredField("mappedStatement");
        field.setAccessible(true);
        MappedStatement mappedStatement = (MappedStatement) field.get(defaultResultSetHandler);
        Configuration configuration = mappedStatement.getConfiguration();
        Iterator iterator = result.iterator();

        while (iterator.hasNext()) {
            Object value = iterator.next();
            if (null != value && !FieldSetPropertyHelper.foreachValue(configuration, value, consumer)) {
                break;
            }
        }
        return result;
    }

}
