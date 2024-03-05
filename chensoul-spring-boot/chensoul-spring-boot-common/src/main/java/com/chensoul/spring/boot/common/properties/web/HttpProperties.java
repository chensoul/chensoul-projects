package com.chensoul.spring.boot.common.properties.web;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Getter
@Setter
@Accessors(chain = true)
public class HttpProperties {
    private long timeToLiveInDays;

}
