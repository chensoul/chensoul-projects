package com.chensoul.oauth2.configuration;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * JwtAccessTokenConverter Configuration
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Configuration
@AllArgsConstructor
public class JwtAccessTokenConverterConfiguration implements JwtAccessTokenConverterConfigurer {
    /**
     * Set access token converter for JwkVerifyingJwtAccessTokenConverter
     *
     * @param converter the converter to configure
     */
    @Override
    public void configure(final JwtAccessTokenConverter converter) {
        final DefaultUserAuthenticationConverter userTokenConverter = new DefaultUserAuthenticationConverter();
        final DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(userTokenConverter);
        converter.setAccessTokenConverter(accessTokenConverter);
    }
}
