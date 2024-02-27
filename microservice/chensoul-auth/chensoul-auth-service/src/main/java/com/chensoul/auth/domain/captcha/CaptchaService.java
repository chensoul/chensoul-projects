package com.chensoul.auth.domain.captcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface CaptchaService {
    String PARAM_CODE = "code";

    String PARAM_RANDOM_STR = "randstr";

    /**
     * @param request
     * @return
     */
    byte[] generateCode(HttpServletRequest request, HttpServletResponse response);

    /**
     * @param request
     */
    void validateCode(HttpServletRequest request, HttpServletResponse response);
}
