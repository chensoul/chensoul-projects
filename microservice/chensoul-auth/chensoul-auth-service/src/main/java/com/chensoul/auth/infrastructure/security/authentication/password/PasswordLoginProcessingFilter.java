package com.chensoul.auth.infrastructure.security.authentication.password;

import static com.chensoul.auth.infrastructure.security.configuration.LoginProperties.PASSWORD_LOGIN_URL;
import com.chensoul.auth.infrastructure.security.support.CustomWebAuthenticationDetails;
import com.chensoul.auth.model.PasswordLoginRequest;
import com.chensoul.net.InetAddressUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class PasswordLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final ObjectMapper mapper = new ObjectMapper();

    public PasswordLoginProcessingFilter(final AuthenticationManager authenticationManager) {
        super(PASSWORD_LOGIN_URL, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        final PasswordLoginRequest loginRequest = mapper.readValue(request.getReader(), PasswordLoginRequest.class);
        final PasswordLoginAuthentication passwordToken = new PasswordLoginAuthentication(loginRequest.getUsername(), loginRequest.getPassword());
        passwordToken.setDetails(new CustomWebAuthenticationDetails(request, InetAddressUtils.getLocalHostAddress()));

        return this.getAuthenticationManager().authenticate(passwordToken);
    }
}
