package com.chensoul.spring.boot.web.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.spi.ContextAwareBase;
import com.chensoul.spring.boot.common.properties.logging.LoggingProperties;
import com.chensoul.spring.boot.common.properties.logging.LogstashProperties;
import java.net.InetSocketAddress;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.composite.ContextJsonProvider;
import net.logstash.logback.composite.GlobalCustomFieldsJsonProvider;
import net.logstash.logback.composite.loggingevent.ArgumentsJsonProvider;
import net.logstash.logback.composite.loggingevent.LogLevelJsonProvider;
import net.logstash.logback.composite.loggingevent.LoggerNameJsonProvider;
import net.logstash.logback.composite.loggingevent.LoggingEventFormattedTimestampJsonProvider;
import net.logstash.logback.composite.loggingevent.LoggingEventJsonProviders;
import net.logstash.logback.composite.loggingevent.LoggingEventPatternJsonProvider;
import net.logstash.logback.composite.loggingevent.LoggingEventThreadNameJsonProvider;
import net.logstash.logback.composite.loggingevent.MdcJsonProvider;
import net.logstash.logback.composite.loggingevent.MessageJsonProvider;
import net.logstash.logback.composite.loggingevent.StackTraceJsonProvider;
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;

/**
 *
 */
@Slf4j
public final class LogbackUtils {

    private static final String CONSOLE_APPENDER_NAME = "CONSOLE";
    private static final String ASYNC_LOGSTASH_APPENDER_NAME = "ASYNC_LOGSTASH";

    /**
     *
     */
    private LogbackUtils() {
    }

    /**
     * <p>addJsonConsoleAppender.</p>
     *
     * @param context      a {@link ch.qos.logback.classic.LoggerContext} object.
     * @param customFields a {@link java.lang.String} object.
     */
    public static void addJsonConsoleAppender(final LoggerContext context, final String customFields) {
        log.info("Initializing Console loggingProperties");

        // More documentation is available at: https://github.com/logstash/logstash-logback-encoder
        final ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(context);
        consoleAppender.setEncoder(compositeJsonEncoder(context, customFields));
        consoleAppender.setName(CONSOLE_APPENDER_NAME);
        consoleAppender.start();

        context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME).detachAppender(CONSOLE_APPENDER_NAME);
        context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME).addAppender(consoleAppender);
    }

    /**
     * <p>addLogstashTcpSocketAppender.</p>
     *
     * @param context            a {@link ch.qos.logback.classic.LoggerContext} object.
     * @param customFields       a {@link java.lang.String} object.
     * @param logstashProperties a {@link LogstashProperties} object.
     */
    public static void addLogstashTcpSocketAppender(final LoggerContext context, final String customFields,
                                                    final LogstashProperties logstashProperties) {
        log.info("Initializing Logstash loggingProperties");

        // More documentation is available at: https://github.com/logstash/logstash-logback-encoder
        final LogstashTcpSocketAppender logstashAppender = new LogstashTcpSocketAppender();
        logstashAppender.addDestinations(new InetSocketAddress(logstashProperties.getHost(), logstashProperties.getPort()));
        logstashAppender.setContext(context);
        logstashAppender.setEncoder(logstashEncoder(customFields));
        logstashAppender.setName(ASYNC_LOGSTASH_APPENDER_NAME);
        logstashAppender.setRingBufferSize(logstashProperties.getRingBufferSize());
        logstashAppender.start();

        context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME).addAppender(logstashAppender);
    }

    /**
     * <p>addContextListener.</p>
     *
     * @param context      a {@link ch.qos.logback.classic.LoggerContext} object.
     * @param customFields a {@link java.lang.String} object.
     * @param properties   a {@link LoggingProperties} object.
     */
    public static void addContextListener(final LoggerContext context, final String customFields, final LoggingProperties properties) {
        final LogbackLoggerContextListener loggerContextListener = new LogbackLoggerContextListener(properties, customFields);
        loggerContextListener.setContext(context);
        context.addListener(loggerContextListener);
    }

    /**
     * @param context
     * @param customFields
     * @return
     */
    private static LoggingEventCompositeJsonEncoder compositeJsonEncoder(final LoggerContext context, final String customFields) {
        final LoggingEventCompositeJsonEncoder compositeJsonEncoder = new LoggingEventCompositeJsonEncoder();
        compositeJsonEncoder.setContext(context);
        compositeJsonEncoder.setProviders(jsonProviders(context, customFields));
        compositeJsonEncoder.start();
        return compositeJsonEncoder;
    }

    /**
     * @param customFields
     * @return
     */
    private static LogstashEncoder logstashEncoder(final String customFields) {
        final LogstashEncoder logstashEncoder = new LogstashEncoder();
        logstashEncoder.setThrowableConverter(throwableConverter());
        logstashEncoder.setCustomFields(customFields);
        return logstashEncoder;
    }

    /**
     * @param context
     * @param customFields
     * @return
     */
    private static LoggingEventJsonProviders jsonProviders(final LoggerContext context, final String customFields) {
        final LoggingEventJsonProviders jsonProviders = new LoggingEventJsonProviders();
        jsonProviders.addArguments(new ArgumentsJsonProvider());
        jsonProviders.addContext(new ContextJsonProvider<>());
        jsonProviders.addGlobalCustomFields(customFieldsJsonProvider(customFields));
        jsonProviders.addLogLevel(new LogLevelJsonProvider());
        jsonProviders.addLoggerName(loggerNameJsonProvider());
        jsonProviders.addMdc(new MdcJsonProvider());
        jsonProviders.addMessage(new MessageJsonProvider());
        jsonProviders.addPattern(new LoggingEventPatternJsonProvider());
        jsonProviders.addStackTrace(stackTraceJsonProvider());
        jsonProviders.addThreadName(new LoggingEventThreadNameJsonProvider());
        jsonProviders.addTimestamp(timestampJsonProvider());
        jsonProviders.setContext(context);
        return jsonProviders;
    }

    /**
     * @param customFields
     * @return
     */
    private static GlobalCustomFieldsJsonProvider<ILoggingEvent> customFieldsJsonProvider(final String customFields) {
        final GlobalCustomFieldsJsonProvider<ILoggingEvent> customFieldsJsonProvider = new GlobalCustomFieldsJsonProvider<>();
        customFieldsJsonProvider.setCustomFields(customFields);
        return customFieldsJsonProvider;
    }

    /**
     * @return
     */
    private static LoggerNameJsonProvider loggerNameJsonProvider() {
        final LoggerNameJsonProvider loggerNameJsonProvider = new LoggerNameJsonProvider();
        loggerNameJsonProvider.setShortenedLoggerNameLength(20);
        return loggerNameJsonProvider;
    }

    /**
     * @return
     */
    private static StackTraceJsonProvider stackTraceJsonProvider() {
        final StackTraceJsonProvider stackTraceJsonProvider = new StackTraceJsonProvider();
        stackTraceJsonProvider.setThrowableConverter(throwableConverter());
        return stackTraceJsonProvider;
    }

    /**
     * @return
     */
    private static ShortenedThrowableConverter throwableConverter() {
        final ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
        throwableConverter.setRootCauseFirst(true);
        return throwableConverter;
    }

    /**
     * @return
     */
    private static LoggingEventFormattedTimestampJsonProvider timestampJsonProvider() {
        final LoggingEventFormattedTimestampJsonProvider timestampJsonProvider = new LoggingEventFormattedTimestampJsonProvider();
        timestampJsonProvider.setTimeZone("UTC");
        timestampJsonProvider.setFieldName("timestamp");
        return timestampJsonProvider;
    }

    /**
     * Logback interceptor is achieved by interceptor file and API.
     * When interceptor file change is detected, the interceptor is reset.
     * This listener ensures that the programmatic interceptor is also re-applied after reset.
     */
    private static class LogbackLoggerContextListener extends ContextAwareBase implements LoggerContextListener {
        private final LoggingProperties loggingProperties;
        private final String customFields;

        /**
         * @param loggingProperties
         * @param customFields
         */
        private LogbackLoggerContextListener(final LoggingProperties loggingProperties, final String customFields) {
            this.loggingProperties = loggingProperties;
            this.customFields = customFields;
        }

        @Override
        public boolean isResetResistant() {
            return true;
        }

        @Override
        public void onStart(final LoggerContext context) {
            if (loggingProperties.isUseJsonFormat()) {
                addJsonConsoleAppender(context, customFields);
            }
            if (loggingProperties.getLogstash().isEnabled()) {
                addLogstashTcpSocketAppender(context, customFields, loggingProperties.getLogstash());
            }
        }

        @Override
        public void onReset(final LoggerContext context) {
            if (loggingProperties.isUseJsonFormat()) {
                addJsonConsoleAppender(context, customFields);
            }
            if (loggingProperties.getLogstash().isEnabled()) {
                addLogstashTcpSocketAppender(context, customFields, loggingProperties.getLogstash());
            }
        }

        @Override
        public void onStop(final LoggerContext context) {
            // Nothing to do.
        }

        @Override
        public void onLevelChange(final ch.qos.logback.classic.Logger logger, final Level level) {
            // Nothing to do.
        }
    }
}
