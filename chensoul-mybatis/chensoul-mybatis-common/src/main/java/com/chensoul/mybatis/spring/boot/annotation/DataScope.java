package com.chensoul.mybatis.spring.boot.annotation;

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
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DataScope {

    /**
     * @return
     */
    String type() default "";

    /**
     * @return
     */
    DataColumn[] value() default {};

    /**
     * @return
     */
    boolean ignore() default false;

    /**
     * @return
     */
    String[] ignoreMethods() default {};

}
