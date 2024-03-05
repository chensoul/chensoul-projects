package com.chensoul.spring.boot.web.webmvc;

import com.chensoul.spring.boot.common.properties.CoreProperties;
import com.chensoul.spring.boot.common.properties.audit.AuditEngineProperties;
import com.chensoul.spring.boot.common.properties.web.ClientProperties;
import com.chensoul.spring.client.ClientInfoOptions;
import com.chensoul.spring.client.ClientInfoRemoteAddrFilter;
import com.chensoul.spring.client.ClientInfoThreadLocalFilter;
import java.util.Arrays;
import javax.servlet.Servlet;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@EnableConfigurationProperties(CoreProperties.class)
public class ClientInfoConfiguration {
    @Bean
    @ConditionalOnMissingBean(name = "clientInfoThreadLocalFilter")
    public FilterRegistrationBean<ClientInfoThreadLocalFilter> clientInfoThreadLocalFilter(
        final CoreProperties properties) {
        final FilterRegistrationBean bean = new FilterRegistrationBean<ClientInfoThreadLocalFilter>();
        final AuditEngineProperties audit = properties.getAudit().getEngine();
        final ClientInfoOptions options = ClientInfoOptions.builder()
            .clientIpHeaderName(audit.getClientIpHeaderName())
            .serverIpHeaderName(audit.getServerIpHeaderName())
            .useServerHostAddress(audit.isUseServerHostAddress())
            .supportedHeaders(audit.getSupportedHeaders())
            .build();
        bean.setFilter(new ClientInfoThreadLocalFilter(options));
        bean.setUrlPatterns(Arrays.asList("/*"));
        bean.setName("Client Info Logging Filter");
        bean.setAsyncSupported(true);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    @ConditionalOnMissingBean(name = "clientInfoRemoteAddrFilter")
    public FilterRegistrationBean<ClientInfoRemoteAddrFilter> clientInfoRemoteAddrFilter(
        final CoreProperties properties) {
        final FilterRegistrationBean bean = new FilterRegistrationBean();
        final ClientProperties clientProperties = properties.getServer().getClient();
        final ClientInfoRemoteAddrFilter filter = new ClientInfoRemoteAddrFilter();
        filter.setAllow(clientProperties.getAllowedClientIpRegex());
        filter.setDeny(clientProperties.getDeniedClientIpRegex());
        filter.setDenyStatus(HttpStatus.UNAUTHORIZED.value());
        bean.setFilter(filter);
        bean.setUrlPatterns(Arrays.asList("/*"));
        bean.setName("clientInfoRemoteAddrFilter");
        bean.setEnabled(clientProperties.isEnabled());
        return bean;
    }

}
