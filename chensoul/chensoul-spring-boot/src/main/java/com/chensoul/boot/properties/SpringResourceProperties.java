package com.chensoul.boot.properties;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.core.io.Resource;

/**
 * This is {@link SpringResourceProperties}.
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class SpringResourceProperties implements Serializable {
    private static final long serialVersionUID = 4142130961445546358L;

    /**
     * The location of the resource. Resources can be URLs, or
     * files found either on the classpath or outside somewhere
     * in the file system.
     * <p>
     * In the event the configured resource is a Groovy script, specially if the script set to reload on changes,
     * you may need to adjust the total number of {@code inotify} instances.
     * On Linux, you may need to add the following line to {@code /etc/sysctl.conf}:
     * {@code fs.inotify.max_user_instances = 256}.
     * <p>
     * You can check the current value via {@code cat /proc/sys/fs/inotify/max_user_instances}.
     * <p>
     * In situations and scenarios where CAS is able to automatically watch the underlying resource
     * for changes and detect updates and modifications dynamically, you may be able to specify the following
     * setting as either an environment variable or system property with a value of {@code false} to disable
     * the resource watcher: {@code org.apereo.cas.util.io.PathWatcherService}.
     */
    private transient Resource location;
}
