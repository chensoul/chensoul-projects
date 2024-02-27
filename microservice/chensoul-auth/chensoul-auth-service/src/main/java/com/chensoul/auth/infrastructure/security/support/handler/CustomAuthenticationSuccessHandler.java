package com.chensoul.auth.infrastructure.security.support.handler;

import com.chensoul.auth.model.PasswordLoginResponse;
import com.chensoul.jackson.utils.JsonUtils;
import com.chensoul.util.R;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
        log.info("login success");

        final PasswordLoginResponse passwordLoginResponse = new PasswordLoginResponse();
        passwordLoginResponse.setUsername(authentication.getName());
        passwordLoginResponse.setToken(UUID.randomUUID().toString());

        JsonUtils.render(response, R.ok(passwordLoginResponse));
    }
}
