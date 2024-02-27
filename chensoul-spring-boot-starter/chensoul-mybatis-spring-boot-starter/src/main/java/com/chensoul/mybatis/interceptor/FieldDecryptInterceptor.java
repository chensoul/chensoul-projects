package com.chensoul.mybatis.interceptor;

import com.chensoul.mybatis.annotation.FieldBind;
import com.chensoul.mybatis.annotation.FieldEncrypt;
import com.chensoul.mybatis.configuration.EncryptorProperties;
import com.chensoul.mybatis.encrypt.Encryptor;
import com.chensoul.mybatis.encrypt.FieldBinder;
import com.chensoul.mybatis.encrypt.FieldSetProperty;
import com.chensoul.mybatis.encrypt.FieldSetPropertyHelper;
import com.chensoul.mybatis.encrypt.InterceptorHelper;
import java.sql.Statement;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
@Slf4j
@Data
public class FieldDecryptInterceptor implements Interceptor {

    private Encryptor encryptor;

    private FieldBinder fieldBinder;

    private EncryptorProperties encryptorProperties;

    /**
     * @param encryptor
     * @param fieldBinder
     * @param encryptorProperties
     */
    public FieldDecryptInterceptor(Encryptor encryptor, FieldBinder fieldBinder,
                                   EncryptorProperties encryptorProperties) {
        this.encryptor = encryptor;
        this.fieldBinder = fieldBinder;
        this.encryptorProperties = encryptorProperties;

        FieldSetPropertyHelper.init(null != encryptor, null != fieldBinder);
    }

    /**
     * @param invocation
     * @return
     * @throws Throwable
     */
    public Object intercept(Invocation invocation) throws Throwable {
        return InterceptorHelper.decrypt(invocation, (metaObject, fieldSetProperty) -> {
            decrypt(this.encryptor, this.fieldBinder, this.encryptorProperties, metaObject, fieldSetProperty);
        });
    }

    /**
     * @param encryptor
     * @param fieldBinder
     * @param encryptorProperties
     * @param metaObject
     * @param fieldSetProperty
     */
    public void decrypt(Encryptor encryptor, FieldBinder fieldBinder, EncryptorProperties encryptorProperties,
                        MetaObject metaObject, FieldSetProperty fieldSetProperty) {
        String fieldName = fieldSetProperty.getFieldName();
        Object value = metaObject.getValue(fieldName);
        if (null != value) {
            if (null != encryptor && value instanceof String) {
                try {
                    FieldEncrypt fieldEncrypt = fieldSetProperty.getFieldEncrypt();
                    if (null != fieldEncrypt) {
                        value = InterceptorHelper.getEncryptor(encryptor, fieldEncrypt.encryptor())
                                .decrypt(fieldEncrypt.algorithm(), fieldEncrypt.password(), (String) value, null);
                    }
                } catch (Exception e) {
                    log.error("field decrypt", e);
                }
            }

            if (null != fieldBinder) {
                FieldBind fieldBind = fieldSetProperty.getFieldBind();
                if (null != fieldBind) {
                    fieldBinder.setMetaObject(fieldBind, value, metaObject);
                }
            }
            metaObject.setValue(fieldName, value);
        }
    }

    /**
     * @param var1
     * @return
     */
    public Object plugin(Object var1) {
        return var1 instanceof ResultSetHandler ? Plugin.wrap(var1, this) : var1;
    }
}
