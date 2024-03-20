
package com.chensoul.spring.cloud.openfeign.sentinel;

/*-
 * #%L
 * Chensoul :: Spring Cloud OpenFeign
 * %%
 * Copyright (C) 2023 - 2024 chensoul.cc
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.chensoul.spring.cloud.openfeign.sentinel.ext.AllowHeaderRequestOriginParser;
import com.chensoul.spring.cloud.openfeign.sentinel.handle.JsonBlockExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * sentinel 配置
 */
@AutoConfiguration
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
@ConditionalOnProperty(name = "feign.sentinel.enabled", havingValue = "true")
public class SentinelFeignConfiguration {

    /**
     * @return
     */
//    @Bean
//    @Scope("prototype")
//    @ConditionalOnMissingBean
//    public Feign.Builder autoFallbackSentinelFeignBuilder() {
//        return AutoFallbackSentinelFeign.builder();
//    }

    /**
     * @param objectMapper
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public BlockExceptionHandler blockExceptionHandler(ObjectMapper objectMapper) {
        return new JsonBlockExceptionHandler(objectMapper);
    }

    /**
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public RequestOriginParser requestOriginParser() {
        return new AllowHeaderRequestOriginParser();
    }

}
