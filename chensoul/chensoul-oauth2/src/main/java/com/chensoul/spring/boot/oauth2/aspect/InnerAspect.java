package com.chensoul.spring.boot.oauth2.aspect;

import com.chensoul.spring.boot.oauth2.Inner;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 服务间接口不鉴权处理逻辑
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class InnerAspect implements Ordered {

    private final HttpServletRequest request;

    /**
     * @param point
     * @param inner
     * @return
     */
    @SneakyThrows
    @Around("@within(inner) || @annotation(inner)")
    public Object around(ProceedingJoinPoint point, Inner inner) {
        // 实际注入的inner实体由表达式后一个注解决定，即是方法上的@Inner注解实体，若方法上无@Inner注解，则获取类上的
        if (inner == null) {
            Class<?> clazz = point.getTarget().getClass();
            inner = AnnotationUtils.findAnnotation(clazz, Inner.class);
        }
        String header = request.getHeader("from");
        if (inner.value() && !StringUtils.equals("Y", header)) {
            if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                log.warn("访问接口 {} 没有权限", point.getSignature().getName());
                throw new AccessDeniedException("无权限访问接口");
            }
        }
        return point.proceed();
    }

    /**
     * @return
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

}
