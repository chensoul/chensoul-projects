package com.chensoul.auth.infrastructure.security.authentication.password;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Component
@Slf4j
public class PasswordLoginAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final PasswordLoginAuthentication passwordToken = (PasswordLoginAuthentication) authentication;
        log.info("username={}", passwordToken.getPrincipal());

        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(passwordToken.getPrincipal(), null, Collections.emptyList());
        authToken.setDetails(passwordToken.getDetails());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        return authToken;
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.isAssignableFrom(PasswordLoginAuthentication.class);
    }
}
