package com.chensoul.auth.infrastructure.security.support.handler;

import com.chensoul.jackson.utils.JsonUtils;
import com.chensoul.util.R;
import com.chensoul.util.ResultCode;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Component
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception) throws IOException, ServletException {
        log.info("login fail", exception);

        JsonUtils.render(response, R.error(ResultCode.UNAUTHORIZED.getCode(), exception.getMessage()));
    }
}
