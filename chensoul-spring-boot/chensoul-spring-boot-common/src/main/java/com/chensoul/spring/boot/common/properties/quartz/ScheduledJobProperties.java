package com.chensoul.spring.boot.common.properties.quartz;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * This is {@link ScheduledJobProperties}.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ScheduledJobProperties implements Serializable {

    private static final long serialVersionUID = 9059671958275130605L;

    /**
     * Scheduler settings to indicate how often the job should run.
     */
    @NestedConfigurationProperty
    private SchedulingProperties schedule = new SchedulingProperties();
}
