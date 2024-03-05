package com.chensoul.spring.cloud.openfeign.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Backoff {

    /**
     * @return
     */
    long delay() default 1000L;

    /**
     * @return
     */
    long maxDelay() default 0L;

    /**
     * @return
     */
    double multiplier() default 0.0D;

}
