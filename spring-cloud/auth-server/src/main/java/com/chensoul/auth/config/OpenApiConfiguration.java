package com.chensoul.auth.config;

import com.chensoul.util.StringUtils;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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

/**
 * springdoc openapi 使用授权码模式，请参考 <a href="https://github.com/chensoul/Microservices-with-Spring-Boot-and-Spring-Cloud-Third-Edition/tree/springboot3.2-jdk21-maven/Chapter11-oauth2">https://github.com/chensoul/Microservices-with-Spring-Boot-and-Spring-Cloud-Third-Edition/tree/springboot3.2-jdk21-maven/Chapter11-oauth2</a>。
 */
@io.swagger.v3.oas.annotations.security.SecurityScheme(
    name = "security_auth", type = SecuritySchemeType.OAUTH2,
    flows = @io.swagger.v3.oas.annotations.security.OAuthFlows(
        clientCredentials = @io.swagger.v3.oas.annotations.security.OAuthFlow(
//            authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
            tokenUrl = "${springdoc.oAuthFlow.tokenUrl}",

            scopes = {
                @OAuthScope(name = "openid", description = "for openid"),
                @OAuthScope(name = "profile", description = "for profile"),
                @OAuthScope(name = "message.read", description = "read scope"),
                @OAuthScope(name = "message.write", description = "write scope")
            }
        )
    )
)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class OpenApiConfiguration {
    @Bean
    public OpenAPI customOpenAPI(@Nullable BuildProperties build, Environment env, @Value("${springdoc.oAuthFlow.tokenUrl}") String tokenUrl) {
        if (build == null) {
            build = new BuildProperties(new Properties());
        }

        OpenAPI openAPI = new OpenAPI();

        Info info = new Info().title(StringUtils.defaultIfBlank(build.getName(), env.getProperty("spring.application.name")))
            .version(build.getVersion())
            .description(build.get("description"))
            .license(new License().name("Apache 2.0"));
        openAPI.info(info);

//        Scopes scopes = new Scopes()
//            .addString("openid", "for openid")
//            .addString("profile", "for profile")
//            .addString("message.read", "for read message")
//            .addString("message.write", "for write message");
//        openAPI.components(new Components().addSecuritySchemes(CLIENT_CREDENTIALS.getValue(), new SecurityScheme()
//                    .type(SecurityScheme.Type.OAUTH2)
//                    .flows(new OAuthFlows().clientCredentials(new OAuthFlow().tokenUrl(tokenUrl).scopes(scopes)))
//                )
//            )
//            .security(Arrays.asList(new SecurityRequirement().addList(CLIENT_CREDENTIALS.getValue())));

        return openAPI;
    }
}
