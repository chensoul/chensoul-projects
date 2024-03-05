package com.chensoul.spring.boot.web.webmvc.cookie.mgmr;

import com.chensoul.crypto.CipherExecutor;
import com.chensoul.spring.boot.common.properties.cookie.PinnableCookieProperties;
import com.chensoul.spring.boot.web.webmvc.cookie.CookieSameSitePolicy;
import com.chensoul.spring.boot.web.webmvc.cookie.InvalidCookieException;
import com.chensoul.spring.client.ClientInfoHolder;
import com.chensoul.spring.util.HttpRequestUtils;
import com.chensoul.util.StringUtils;
import java.io.Serializable;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * The {@link DefaultCasCookieValueManager} is responsible for creating
 * the CAS SSO cookie and encrypting and signing its value.
 * <p>
 * This class by default ({@code CookieProperties.isPinToSession=true}) ensures the cookie is used on a
 * request from same IP and with the same user-agent as when cookie was created.
 * The client info (with original client ip) may be null if cluster failover occurs and session replication not working.
 *
 * @author Misagh Moayyed
 * @since 4.1
 */
@Slf4j
public class DefaultCasCookieValueManager extends EncryptedCookieValueManager {
    private static final char COOKIE_FIELD_SEPARATOR = '@';

    private static final int COOKIE_FIELDS_LENGTH = 3;

    private static final long serialVersionUID = -2696352696382374584L;

    private final PinnableCookieProperties cookieProperties;


    public DefaultCasCookieValueManager(final CipherExecutor<Serializable, Serializable> cipherExecutor,
                                        final CookieSameSitePolicy cookieSameSitePolicy,
                                        final PinnableCookieProperties cookieProperties) {
        super(cipherExecutor, cookieSameSitePolicy);
        this.cookieProperties = cookieProperties;
    }

    @Override
    protected String buildCompoundCookieValue(final String givenCookieValue, final HttpServletRequest request) {
        val builder = new StringBuilder(givenCookieValue);

        if (cookieProperties.isPinToSession()) {
            val clientInfo = ClientInfoHolder.getClientInfo();
            if (clientInfo != null) {
                val clientLocation = clientInfo.getClientIp();
                builder.append(COOKIE_FIELD_SEPARATOR).append(clientLocation);
            }
            val userAgent = HttpRequestUtils.getUserAgent(request);
            if (StringUtils.isBlank(userAgent)) {
                throw new IllegalStateException("Request does not specify a user-agent");
            }
            builder.append(COOKIE_FIELD_SEPARATOR).append(userAgent);
        } else {
            log.trace("Cookie session-pinning is disabled");
        }

        return builder.toString();
    }

    @Override
    protected String obtainValueFromCompoundCookie(final String value, final HttpServletRequest request) {
        String[] cookieParts = StringUtils.split(value, COOKIE_FIELD_SEPARATOR);

        String cookieValue = cookieParts[0];
        if (!cookieProperties.isPinToSession()) {
            log.trace("Cookie session-pinning is disabled. Returning cookie value as it was provided");
            return cookieValue;
        }

        if (cookieParts.length != COOKIE_FIELDS_LENGTH) {
            throw new InvalidCookieException("Invalid cookie. Required fields are missing");
        }
        val cookieUserAgent = cookieParts[1];

        if (Stream.of(cookieValue, cookieUserAgent).anyMatch(StringUtils::isBlank)) {
            throw new InvalidCookieException("Invalid cookie. Required fields are empty");
        }

        val clientInfo = ClientInfoHolder.getClientInfo();
        if (clientInfo == null) {
            val message = String.format("Unable to match required remote address %s because client ip at time of cookie creation is unknown", clientInfo.getClientIp());
            log.warn(message);
            throw new InvalidCookieException(message);
        }

        String agent = HttpRequestUtils.getUserAgent(request);
        if (!cookieUserAgent.equals(agent)) {
            String message = String.format("Invalid cookie. Required user-agent %s does not match %s", cookieUserAgent, agent);
            log.warn(message);
            throw new InvalidCookieException(message);
        }
        return cookieValue;
    }
}
