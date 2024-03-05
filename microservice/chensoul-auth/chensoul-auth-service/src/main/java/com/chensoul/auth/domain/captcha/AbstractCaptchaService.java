package com.chensoul.auth.domain.captcha;

import com.chensoul.exception.SystemException;
import com.chensoul.util.StringUtils;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FastByteArrayOutputStream;

/**
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@AllArgsConstructor
public abstract class AbstractCaptchaService implements CaptchaService {
    protected static final Integer DEFAULT_IMAGE_WIDTH = 100;
    protected static final Integer DEFAULT_IMAGE_HEIGHT = 40;
    protected static final Integer DEFAULT_IMAGE_LEN = 4;


    @Override
    public byte[] generateCode(final HttpServletRequest request, final HttpServletResponse response) {
        final String randStr = request.getParameter(PARAM_RANDOM_STR);
        if (StringUtils.isBlank(randStr)) {
            throw new SystemException("randStr不能为空");
        }

        final Captcha captcha = new SpecCaptcha(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT, DEFAULT_IMAGE_LEN);
        captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);

        @Cleanup final FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        captcha.out(os);

        onGenerateSuccess(randStr, captcha.text());

        return os.toByteArray();
    }

    protected abstract void onGenerateSuccess(String randStr, String code);

    @Override
    public void validateCode(final HttpServletRequest request, final HttpServletResponse response) {
        final String code = request.getParameter(PARAM_CODE);
        final String randStr = request.getParameter(PARAM_RANDOM_STR);

        if (StringUtils.isBlank(randStr)) {
            throw new SystemException("randStr不能为空");
        }
        if (StringUtils.isBlank(code)) {
            throw new SystemException("code不能为空");
        }

        beforeValidate(request, response);
        if (!validateCode(code, randStr)) {
            onValidateFail(request, code, randStr);
            throw new SystemException("验证码错误或者已过期");
        } else {
            onValidateSuccess(code, randStr);
        }
    }

    protected void beforeValidate(final HttpServletRequest request, final HttpServletResponse response) {

    }


    protected void onValidateSuccess(final String code, final String randStr) {

    }

    protected void onValidateFail(final HttpServletRequest request, final String code, final String randStr) {
    }

    protected abstract boolean validateCode(String code, String randStr);

}
