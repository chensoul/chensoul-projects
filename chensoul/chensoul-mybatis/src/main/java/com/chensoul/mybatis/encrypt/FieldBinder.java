package com.chensoul.mybatis.encrypt;

import com.chensoul.mybatis.annotation.FieldBind;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface FieldBinder {

    /**
     * @param fieldBind
     * @param value
     * @param metaObject
     */
    void setMetaObject(FieldBind fieldBind, Object value, MetaObject metaObject);

}
