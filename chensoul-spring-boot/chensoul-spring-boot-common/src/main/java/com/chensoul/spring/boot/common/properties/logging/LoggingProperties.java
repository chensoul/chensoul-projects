package com.chensoul.spring.boot.common.properties.logging;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This is {@link LoggingProperties}.
 */
@Getter
@Setter
@Accessors(chain = true)
public class LoggingProperties implements Serializable {

    private static final long serialVersionUID = 7455171260665661949L;

    /**
     * Allow CAS to add http request details into the logging's MDC support.
     * Mapped Diagnostic Context is essentially a map maintained by the logging
     * framework where the application code provides key-value pairs which can then be
     * inserted by the logging framework in log messages. MDC data can also be highly
     * helpful in filtering messages or triggering certain actions.
     */
    private boolean mdcEnabled = true;

    private boolean useJsonFormat = false;

    private final LogstashProperties logstash = new LogstashProperties();

}
