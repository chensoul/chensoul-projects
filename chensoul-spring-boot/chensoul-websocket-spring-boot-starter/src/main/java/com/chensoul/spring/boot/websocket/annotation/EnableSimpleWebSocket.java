package com.chensoul.spring.boot.websocket.annotation;

import com.chensoul.spring.boot.websocket.configuration.SimpleWebSocketConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SimpleWebSocketConfiguration.class})
public @interface EnableSimpleWebSocket {

}
