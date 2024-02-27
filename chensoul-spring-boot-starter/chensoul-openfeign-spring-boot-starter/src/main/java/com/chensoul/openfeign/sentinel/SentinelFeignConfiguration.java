/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.chensoul.openfeign.sentinel;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.chensoul.openfeign.sentinel.ext.AllowHeaderRequestOriginParser;
import com.chensoul.openfeign.sentinel.handle.JsonBlockExceptionHandler;
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
