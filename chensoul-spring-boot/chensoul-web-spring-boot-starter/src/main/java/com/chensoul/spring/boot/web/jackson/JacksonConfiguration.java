package com.chensoul.spring.boot.web.jackson;

import com.chensoul.jackson.ComponentSerializers;
import com.chensoul.jackson.ComponentSerializersConfigurer;
import com.chensoul.jackson.support.DefaultComponentSerializers;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@AutoConfiguration
public class JacksonConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public ComponentSerializers componentSerializers(
        final ObjectProvider<List<ComponentSerializersConfigurer>> configurers) {
        final ComponentSerializers serializers = new DefaultComponentSerializers();

        configurers.ifAvailable(cfgs -> cfgs.forEach(c -> {
            log.trace("Configuring component serializer [{}]", c.getName());
            c.configure(serializers);
        }));

        return serializers;
    }
}
