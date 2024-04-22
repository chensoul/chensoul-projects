package com.chensoul.spring.boot.web.webmvc;

import com.chensoul.spring.boot.common.properties.CoreProperties;
import com.chensoul.spring.boot.web.webmvc.filter.CachingHttpHeadersFilter;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Slf4j
@AutoConfiguration
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(CoreProperties.class)
public class WebMvcConfiguration  {
    /**
     * @return
     */
    @Bean
    public CachingHttpHeadersFilter cachingHttpHeadersFilter(final CoreProperties properties) {
        // Use a cache support that only match selected paths
        return new CachingHttpHeadersFilter(TimeUnit.DAYS.toMillis(properties.getServer().getClient().getHttpProperties().getTimeToLiveInDays()));
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        final CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter() {
            @Override
            protected void afterRequest(HttpServletRequest request, String message) {

            }
        };
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setIncludeClientInfo(true);
        return filter;
    }
}


