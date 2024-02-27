package com.chensoul.configuration.startup;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.core.metrics.jfr.FlightRecorderApplicationStartup;

/**
 * Copied from cas: <a href="https://github.com/apereo/cas/blob/master/core/cas-server-core-util-api/src/main/java/org/apereo/cas/util/app/ApplicationUtils.java">ApplicationUtils</a>
 * <p>
 * Usage:
 * <pre><code>
 * public class WebApplication {
 *     public static void main(final String[] args) {
 *         List<Class> applicationClasses = getApplicationSources(args);
 *         new SpringApplicationBuilder()
 *             .logStartupInfo(true)
 *             .applicationStartup(ApplicationUtils.getApplicationStartup())
 *             .run(args);
 *     }
 *
 *     protected static List<Class> getApplicationSources(final String[] args) {
 *         List<Class> applicationClasses = new ArrayList<>();
 *         applicationClasses.add(WebApplication.class);
 *         ApplicationUtils.getApplicationEntrypointInitializers()
 *             .forEach(init -> {
 *                 init.initialize(args);
 *                 applicationClasses.addAll(init.getApplicationSources(args));
 *             });
 *         return applicationClasses;
 *     }
 * }
 * </code></pre>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@UtilityClass
public class ApplicationUtils {
    private static final int APPLICATION_EVENTS_CAPACITY = 5_000;

    public static List<ApplicationEntrypointInitializer> getApplicationEntrypointInitializers() {
        final List<ApplicationEntrypointInitializer> initializers = new ArrayList<>();
        final ServiceLoader<ApplicationEntrypointInitializer> loader = ServiceLoader.load(ApplicationEntrypointInitializer.class);
        for (final ApplicationEntrypointInitializer initializer : loader) {
            if (initializer != null) {
                initializers.add(initializer);
            }
        }
        initializers.sort(AnnotationAwareOrderComparator.INSTANCE);
        return initializers;
    }

    /**
     * Gets application startup.
     *
     * @return the application startup
     */
    public static ApplicationStartup getApplicationStartup() {
        final String type = StringUtils.defaultIfBlank(System.getProperty("CAS_APP_STARTUP"), "default");
        if (StringUtils.equalsIgnoreCase("jfr", type)) {
            return new FlightRecorderApplicationStartup();
        }
        if (StringUtils.equalsIgnoreCase("buffering", type)) {
            return new BufferingApplicationStartup(APPLICATION_EVENTS_CAPACITY);
        }
        return ApplicationStartup.DEFAULT;
    }

}
