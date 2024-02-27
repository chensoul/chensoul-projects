package com.chensoul.oauth2.support;

import com.chensoul.constant.ResultCode;
import com.chensoul.util.R;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.HtmlUtils;

/**
 *
 */
@Slf4j
public class ResultMessageResolver implements MessageSourceAware {
    private static MessageSource messageSource;

    /**
     * @param messageSource message source to be used by this object
     */
    @Override
    public void setMessageSource(MessageSource messageSource) {
        ResultMessageResolver.messageSource = messageSource;
    }

    /**
     * @param e
     * @return
     */
    public static R<String> resolver(Throwable e) {
        Throwable rootCause = ExceptionUtils.getRootCause(e);
        int errorCode = ResultCode.SYSTEM_ERROR.getCode();
        String errorMessage = rootCause.getMessage();

        if (messageSource == null) {
            return R.error(errorCode, errorMessage);
        }

        Locale locale = getLocale();

        if (rootCause instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) rootCause;
            String resourceKey = OAuth2Exception.class.getSimpleName() + '.' + oAuth2Exception.getOAuth2ErrorCode();
            if (errorMessage != null) {
                errorMessage = HtmlUtils.htmlEscape(errorMessage);
            }
            errorCode = oAuth2Exception.getHttpErrorCode();
            errorMessage = messageSource.getMessage(resourceKey, null, errorMessage, locale);
        } else if (rootCause instanceof AuthenticationException) {
            String resourceKey = AuthenticationException.class.getSimpleName() + '.' + getSimpleNamePrefix(rootCause.getClass());

            errorCode = ResultCode.UNAUTHORIZED.getCode();
            errorMessage = messageSource.getMessage(resourceKey, null, errorMessage, locale);
        } else if (rootCause instanceof AccessDeniedException) {
            errorCode = ResultCode.FORBIDDEN.getCode();
            errorMessage = messageSource.getMessage("OAuth2Exception.access_denied", null, errorMessage, locale);
        } else {
            errorMessage = messageSource.getMessage(errorMessage, null, errorMessage, locale);
        }

        return R.error(errorCode, errorMessage);
    }

    private static Locale getLocale() {
        Locale locale = Locale.getDefault();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            locale = RequestContextUtils.getLocale(((ServletRequestAttributes) requestAttributes).getRequest());
        }

        return locale;
    }

    /**
     * @param clazz
     * @return
     */
    private static String getSimpleNamePrefix(Class<?> clazz) {
        String simpleName = clazz.getSimpleName();
        String simpleNamePrefix = simpleName.substring(0, simpleName.indexOf(Exception.class.getSimpleName()));
        return StringUtils.uncapitalize(simpleNamePrefix);
    }
}
