package com.chensoul.spring.boot.web.logging;

import ch.qos.logback.classic.LoggerContext;
import com.chensoul.spring.boot.common.properties.CoreProperties;
import com.chensoul.spring.boot.common.properties.logging.LogstashProperties;
import static com.chensoul.spring.boot.web.logging.LogbackUtils.addContextListener;
import static com.chensoul.spring.boot.web.logging.LogbackUtils.addJsonConsoleAppender;
import static com.chensoul.spring.boot.web.logging.LogbackUtils.addLogstashTcpSocketAppender;
import com.chensoul.jackson.utils.JsonUtils;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

/**
 *
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@AutoConfiguration
@EnableConfigurationProperties(CoreProperties.class)
public class LoggingConfiguration {
    /**
     * @param env
     * @param coreProperties
     */
    public LoggingConfiguration(final Environment env, final CoreProperties coreProperties) {
        final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        final Map<String, String> map = new HashMap<>();
        map.put("app_name", env.getProperty("spring.application.name", "application"));
        map.put("app_port", env.getProperty("server.port", "8080"));
        final String customFields = JsonUtils.toJson(map);

        final LogstashProperties logstashProperties = coreProperties.getLogging().getLogstash();

        if (coreProperties.getLogging().isUseJsonFormat()) {
            addJsonConsoleAppender(context, customFields);
        }
        if (logstashProperties.isEnabled()) {
            addLogstashTcpSocketAppender(context, customFields, logstashProperties);
        }
        if (coreProperties.getLogging().isUseJsonFormat() || logstashProperties.isEnabled()) {
            addContextListener(context, customFields, coreProperties.getLogging());
        }
    }
}
