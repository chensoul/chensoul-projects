package org.springframework.cloud.openfeign;

import com.chensoul.jackson.utils.JsonUtils;
import com.chensoul.util.R;
import com.chensoul.util.ResultCode;
import com.fasterxml.jackson.core.type.TypeReference;
import feign.FeignException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * fallback 代理处理
 */
@Slf4j
@AllArgsConstructor
public class HystrixFeignFallback<T> implements MethodInterceptor {

    private final Class<T> targetType;
    private final String targetName;
    private final Throwable cause;


    @Override
    @SneakyThrows
    public Object intercept(final Object o, final Method method, final Object[] objects, final MethodProxy methodProxy) {
        final Class<?> returnType = method.getReturnType();
        if (R.class != returnType) {
            return null;
        }

        log.warn("{} 服务的 {}.{} 接口降级", this.targetName, this.targetType.getName(), method.getName(), this.cause);

        R result = null;
        if (this.cause instanceof FeignException) {
            final FeignException exception = (FeignException) this.cause;

            final String content = new String(exception.content(), StandardCharsets.UTF_8);
            result = JsonUtils.fromJson(content, new TypeReference<R<?>>() {
            });
        } else {
            result = R.error(ResultCode.SERVICE_UNAVAILABLE.getName());
        }

        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final HystrixFeignFallback<?> that = (HystrixFeignFallback<?>) o;
        return this.targetType.equals(that.targetType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.targetType);
    }

}
