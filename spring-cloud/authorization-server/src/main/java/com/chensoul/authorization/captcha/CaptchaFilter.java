package com.chensoul.authorization.captcha;

import com.chensoul.authorization.captcha.service.CaptchaService;
import com.chensoul.jackson.utils.JsonUtils;
import com.chensoul.util.R;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Captcha Filter
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@AllArgsConstructor
public class CaptchaFilter extends OncePerRequestFilter {
    private CaptchaService captchaService;
    private static final String GRANT_TYPE = "grant_type";

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        final String grantType = request.getParameter(GRANT_TYPE);
        if ("password".equals(grantType)) {
            try {
                this.captchaService.validateCode(request, response);
            } catch (final Exception e) {
                JsonUtils.render(response, R.error(e.getMessage()));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
