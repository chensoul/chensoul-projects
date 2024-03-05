package com.chensoul.spring.boot.oauth2.configuration;

import com.chensoul.spring.boot.oauth2.support.CustomBearerTokenExtractor;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.lang.Nullable;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.client.RestTemplate;

/**
 * @see OAuth2ResourceServerConfiguration
 * @see ResourceServerTokenServicesConfiguration
 */
@Configuration
@Import(OAuthResourceServerConfiguration.ResourceServerConfigurationInner.class)
public class OAuthResourceServerConfiguration {

    /**
     *
     */
    @AllArgsConstructor
    @Configuration
    @EnableConfigurationProperties(PermitUrlProperties.class)
    public class ResourceServerConfigurationInner extends ResourceServerConfigurerAdapter {
        private final ResourceServerTokenServices tokenServices;
        private final AccessDeniedHandler accessDeniedHandler;
        private final AuthenticationEntryPoint authenticationEntryPoint;
        private final PermitUrlProperties properties;

        @Nullable
        private final RestTemplate restTemplate;

        @Override
        public void configure(final HttpSecurity http) throws Exception {
            final ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
            this.properties.getIgnoreUrls().forEach(url -> registry.antMatchers(url).permitAll());
            this.properties.getInnerUrls().forEach(url -> registry.antMatchers(url.getMethod(), url.getUrl()).permitAll());
            this.properties.getAuthenticatedUrls().forEach(url -> registry.antMatchers(url.getMethod(), url.getUrl()).authenticated());
        }

        @Override
        public void configure(final ResourceServerSecurityConfigurer resources) {
            final DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
            final UserAuthenticationConverter userTokenConverter = new DefaultUserAuthenticationConverter();
            accessTokenConverter.setUserTokenConverter(userTokenConverter);
            if (this.tokenServices instanceof RemoteTokenServices) {
                final RemoteTokenServices remoteTokenServices = (RemoteTokenServices) this.tokenServices;
                if (this.restTemplate != null) {
                    remoteTokenServices.setRestTemplate(this.restTemplate);
                }
                remoteTokenServices.setAccessTokenConverter(accessTokenConverter);

                resources.tokenServices(remoteTokenServices);
            } else {
                resources.tokenServices(this.tokenServices);
            }

            resources
                //无状态化,每次访问都需认证
                .stateless(true)
                .tokenExtractor(new CustomBearerTokenExtractor())
                .authenticationEntryPoint(this.authenticationEntryPoint)
                .accessDeniedHandler(this.accessDeniedHandler);
        }
    }

}
