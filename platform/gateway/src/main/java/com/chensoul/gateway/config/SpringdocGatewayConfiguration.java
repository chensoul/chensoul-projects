package com.chensoul.gateway.config;

import com.chensoul.constant.StringPool;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;

/**
 * Springdoc Configuration
 * <p>
 * TODO: listen the spring gateway routers changed event and then refresh swagger urls
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class SpringdocGatewayConfiguration {
    public static final String API_DOCS = "/v3/api-docs/";
    public static final String SERVICE_SUFFIX = "-service";

    private final SwaggerUiConfigProperties swaggerUiConfigProperties;
    private final RouteDefinitionLocator locator;

    public SpringdocGatewayConfiguration(SwaggerUiConfigProperties swaggerUiConfigProperties, RouteDefinitionLocator locator) {
        this.swaggerUiConfigProperties = swaggerUiConfigProperties;
        this.locator = locator;
    }

    /**
     * Auto init swagger url from the spring gateway routers.
     * <p>
     * see gateway.yml routes: RewritePath=/v3/api-docs/(?<path>.*), /$\{path}/v3/api-docs
     *
     * @link https://blog.csdn.net/qq_39609993/article/details/126136180
     */
    @PostConstruct
    public void autoInitSwaggerUrls() {
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = swaggerUiConfigProperties.getUrls();
        if (urls == null) {
            swaggerUiConfigProperties.setUrls(new LinkedHashSet<>());
        }
        swaggerUiConfigProperties.setUseRootPath(true);

        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();

        definitions.stream().filter(routeDefinition -> routeDefinition.getId().endsWith(SERVICE_SUFFIX)).forEach(definition -> {
            String name = definition.getId().replaceAll(SERVICE_SUFFIX, StringPool.EMPTY);
            String url = API_DOCS + name;

            log.info("Add swagger url from the spring gateway routers: name={}, url={}", definition.getId(), url);

            AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl(definition.getId(), url, name);
            swaggerUiConfigProperties.getUrls().add(swaggerUrl);
        });
    }
}
