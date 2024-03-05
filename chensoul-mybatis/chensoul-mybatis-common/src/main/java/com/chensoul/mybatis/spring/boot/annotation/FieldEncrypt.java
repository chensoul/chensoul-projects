package com.chensoul.mybatis.spring.boot.annotation;

import com.chensoul.mybatis.spring.boot.encrypt.Algorithm;
import com.chensoul.mybatis.spring.boot.encrypt.Encryptor;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FieldEncrypt {

    /**
     * @return
     */
    String password() default "";

    /**
     * @return
     */
    Algorithm algorithm() default Algorithm.BASE64;

    /**
     * @return
     */
    Class<? extends Encryptor> encryptor() default Encryptor.class;

}
