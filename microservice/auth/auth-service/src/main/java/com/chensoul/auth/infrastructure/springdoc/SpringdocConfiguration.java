package com.chensoul.auth.infrastructure.springdoc;

import com.chensoul.util.StringUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import java.util.Properties;
import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS;

/**
 * S
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SpringdocConfiguration {
    @Bean
    public OpenAPI customOpenAPI(@Nullable BuildProperties build, Environment env, @Value("${springdoc.oAuthFlow.tokenUrl}") String tokenUrl) {
        if (build == null) {
            build = new BuildProperties(new Properties());
        }
        Scopes scopes = new Scopes()
            .addString("openid", "for openid")
            .addString("profile", "for profile")
            .addString("message.read", "for read message")
            .addString("message.write", "for write message");

        Info info = new Info().title(StringUtils.defaultIfBlank(build.getName(), env.getProperty("spring.application.name")))
            .version(build.getVersion())
            .description(build.get("description"))
            .license(new License().name("Apache 2.0"));

        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes(CLIENT_CREDENTIALS.getValue(), new SecurityScheme()
                    .type(SecurityScheme.Type.OAUTH2)
                    .flows(new OAuthFlows().clientCredentials(new OAuthFlow().tokenUrl(tokenUrl).scopes(scopes)))
                )
            )
            .security(Arrays.asList(new SecurityRequirement().addList(CLIENT_CREDENTIALS.getValue())))
            .info(info);
    }
}
