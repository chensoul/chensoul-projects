package com.chensoul.openfeign.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重试注解，作用在 @FeignClient 注解之上
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FeignRetry {

    /**
     * @return
     */
    Backoff backoff() default @Backoff();

    /**
     * @return
     */
    int maxAttempt() default 3;

    /**
     * @return
     */
    Class<? extends Throwable>[] include() default {};

}
