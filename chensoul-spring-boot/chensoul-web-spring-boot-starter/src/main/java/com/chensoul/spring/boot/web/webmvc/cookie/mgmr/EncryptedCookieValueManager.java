package com.chensoul.spring.boot.web.webmvc.cookie.mgmr;

import com.chensoul.crypto.CipherExecutor;
import com.chensoul.spring.boot.web.webmvc.cookie.CookieSameSitePolicy;
import com.chensoul.spring.boot.web.webmvc.cookie.CookieValueManager;
import com.chensoul.util.StringUtils;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Provides basic encryption/decryption resource for cookie values.
 *
 * @author Daniel Frett
 * @since 5.3.0
 */
@Slf4j
@RequiredArgsConstructor
public class EncryptedCookieValueManager implements CookieValueManager {
    private static final long serialVersionUID = 6362136147071376270L;

    /**
     * The cipher exec that is responsible for encryption and signing of the cookie.
     */
    private final CipherExecutor<Serializable, Serializable> cipherExecutor;

    @Getter
    private final CookieSameSitePolicy cookieSameSitePolicy;

    @Override
    public final String buildCookieValue(final String givenCookieValue, final HttpServletRequest request) {
        val res = buildCompoundCookieValue(givenCookieValue, request);
        log.trace("Encoding cookie value [{}]", res);
        return cipherExecutor.encode(res, ArrayUtils.EMPTY_OBJECT_ARRAY).toString();
    }

    @Override
    public String obtainCookieValue(final String cookie, final HttpServletRequest request) {
        val decoded = cipherExecutor.decode(cookie, ArrayUtils.EMPTY_OBJECT_ARRAY);
        if (decoded == null) {
            log.trace("Could not decode cookie value [{}] for cookie", cookie);
            return null;
        }
        val cookieValue = decoded.toString();
        log.trace("Decoded cookie value is [{}]", cookieValue);
        if (StringUtils.isBlank(cookieValue)) {
            log.trace("Retrieved decoded cookie value is blank. Failed to decode cookie");
            return null;
        }

        return obtainValueFromCompoundCookie(cookieValue, request);
    }

    /**
     * Build the compound cookie value.
     *
     * @param cookieValue the raw cookie value that is being stored
     * @param request     the current webmvc request
     * @return a compound cookie value that may contain additional data beyond the raw cookieValue
     */
    protected String buildCompoundCookieValue(final String cookieValue, final HttpServletRequest request) {
        return cookieValue;
    }

    /**
     * Obtain the cookie value from the compound cookie value.
     *
     * @param compoundValue The compound cookie value
     * @param request       the current webmvc request
     * @return the original cookie value that was stored in the provided compound value.
     */
    protected String obtainValueFromCompoundCookie(final String compoundValue, final HttpServletRequest request) {
        return compoundValue;
    }
}