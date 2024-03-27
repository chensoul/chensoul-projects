package com.chensoul.spring.support;

import com.chensoul.net.InetAddressUtils;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

/**
 *
 */
public final class ApplicationStartupTraces {

    private static final String SEPARATOR = StringUtils.repeat("-", 58);
    private static final String BREAK = "\n";

    /**
     *
     */
    private ApplicationStartupTraces() {
    }

    /**
     * @param environment
     * @return
     */
    public static String of(final Environment environment) {
        Objects.nonNull(environment);

        return new ApplicationStartupTracesBuilder()
            .append(BREAK)
            .appendSeparator()
            .append(applicationRunningTrace(environment))
            .append(localUrl(environment))
            .append(externalUrl(environment))
            .append(profilesTrace(environment))
            .appendSeparator()
            .append(configServer(environment))
            .build();
    }

    /**
     * @param environment
     * @return
     */
    private static String applicationRunningTrace(final Environment environment) {
        final String applicationName = environment.getProperty("spring.application.name");

        if (StringUtils.isBlank(applicationName)) {
            return "Application is running!";
        }

        return new StringBuilder().append("Application '").append(applicationName).append("' is running!").toString();
    }

    /**
     * @param environment
     * @return
     */
    private static String localUrl(final Environment environment) {
        return url("Local", "localhost", environment);
    }

    /**
     * @param environment
     * @return
     */
    private static String externalUrl(final Environment environment) {
        return url("External", hostAddress(), environment);
    }

    /**
     * @param type
     * @param host
     * @param environment
     * @return
     */
    private static String url(final String type, final String host, final Environment environment) {
        if (notWebEnvironment(environment)) {
            return null;
        }

        return new StringBuilder()
            .append(type)
            .append(": \t")
            .append(protocol(environment))
            .append("://")
            .append(host)
            .append(":")
            .append(port(environment))
            .append(contextPath(environment))
            .toString();
    }

    /**
     * @param environment
     * @return
     */
    private static boolean notWebEnvironment(final Environment environment) {
        return StringUtils.isBlank(environment.getProperty("server.port"));
    }

    /**
     * @param environment
     * @return
     */
    private static String protocol(final Environment environment) {
        if (noKeyStore(environment)) {
            return "http";
        }

        return "https";
    }

    /**
     * @param environment
     * @return
     */
    private static boolean noKeyStore(final Environment environment) {
        return StringUtils.isBlank(environment.getProperty("server.ssl.key-store"));
    }

    /**
     * @param environment
     * @return
     */
    private static String port(final Environment environment) {
        return environment.getProperty("server.port");
    }

    /**
     * @param environment
     * @return
     */
    private static String profilesTrace(final Environment environment) {
        final String[] profiles = environment.getActiveProfiles();

        if (ArrayUtils.isEmpty(profiles)) {
            return null;
        }

        return new StringBuilder().append("Profile(s): \t").append(Stream.of(profiles).collect(Collectors.joining(", "))).toString();
    }

    /**
     * @return
     */
    private static String hostAddress() {
        return InetAddressUtils.getLocalHostAddress();
    }

    /**
     * @param environment
     * @return
     */
    private static String contextPath(final Environment environment) {
        final String contextPath = environment.getProperty("server.servlet.context-path");

        if (StringUtils.isBlank(contextPath)) {
            return "/";
        }

        return contextPath;
    }

    /**
     * @param environment
     * @return
     */
    private static String configServer(final Environment environment) {
        final String status = environment.getProperty("configserver.status");

        if (StringUtils.isBlank(status)) {
            return null;
        }

        return new StringBuilder().append("Config Server: ").append(status).append(BREAK).append(SEPARATOR).append(BREAK).toString();
    }

    /**
     *
     */
    private static class ApplicationStartupTracesBuilder {

        private static final String SPACER = "  ";

        private final StringBuilder trace = new StringBuilder();

        /**
         * @return
         */
        public ApplicationStartupTracesBuilder appendSeparator() {
            trace.append(SEPARATOR).append(BREAK);

            return this;
        }

        /**
         * @param line
         * @return
         */
        public ApplicationStartupTracesBuilder append(final String line) {
            if (line == null) {
                return this;
            }

            trace.append(SPACER).append(line).append(BREAK);

            return this;
        }

        /**
         * @return
         */
        public String build() {
            return trace.toString();
        }
    }
}
