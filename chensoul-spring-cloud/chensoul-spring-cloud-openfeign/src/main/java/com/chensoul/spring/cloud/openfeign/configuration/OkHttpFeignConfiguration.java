/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chensoul.spring.cloud.openfeign.configuration;

import com.chensoul.spring.cloud.openfeign.codec.FeignErrorDecoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "feign.okhttp.enabled", havingValue = "true")
public class OkHttpFeignConfiguration {

    /**
     * @return
     */
    @Bean
    public Logger.Level feignLogger() {
        return Logger.Level.HEADERS;
    }

    /**
     * @param mapper
     * @return
     */
    @Bean
    public Decoder decoder(final ObjectMapper mapper) {
        return new JacksonDecoder(mapper);
    }

    /**
     * @return
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    /**
     * OkHttp 客户端配置
     *
     * @return OkHttp 客户端配
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().retryOnConnectionFailure(true) // 是否开启缓存
                .connectTimeout(10L, TimeUnit.SECONDS) // 连接超时时间
                .readTimeout(10L, TimeUnit.SECONDS) // 读取超时时间
                .writeTimeout(10L, TimeUnit.SECONDS)
                .followRedirects(true) // 是否允许重定向
                .build();
    }

}
