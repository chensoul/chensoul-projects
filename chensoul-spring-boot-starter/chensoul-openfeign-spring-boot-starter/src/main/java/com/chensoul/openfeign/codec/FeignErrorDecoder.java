package com.chensoul.openfeign.codec;

import com.chensoul.jackson.utils.JsonUtils;
import com.chensoul.util.R;
import feign.FeignException;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 自定义feign错误响应数据
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class FeignErrorDecoder extends ErrorDecoder.Default {

    @Override
    @SneakyThrows
    public Exception decode(final String methodKey, final feign.Response response) {
        final Exception exception = super.decode(methodKey, response);
        if (exception instanceof RetryableException) {
            return exception;
        }
        if (exception instanceof FeignException) {
            final FeignException feignException = (FeignException) exception;
            final String errorMessage = FeignErrorDecoder.parseResponse(feignException);
            if (errorMessage != null) {
                return new RuntimeException(errorMessage);
            }

            return new RuntimeException("FeignException." + feignException.getClass().getSimpleName());
        }
        return exception;
    }

    private static String parseResponse(final FeignException feignException) {
        final ByteBuffer responseBody = feignException.responseBody().get();

        String errorMessage = null;
        try {
            errorMessage = StandardCharsets.UTF_8.newDecoder().decode(responseBody.asReadOnlyBuffer()).toString();
        } catch (final CharacterCodingException e) {
            throw new RuntimeException("内部服务返回内容编码错误");
        }

        try {
            final R result = JsonUtils.fromJson(errorMessage, R.class);
            return result.getMessage();
        } catch (final Exception e) {
            return null;
        }
    }
}
