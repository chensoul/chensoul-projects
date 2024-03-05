package com.chensoul.spring.boot.web.webmvc.cookie;

import com.chensoul.spring.boot.common.properties.cookie.CookieProperties;
import com.chensoul.spring.boot.web.webmvc.cookie.gen.CookieRetrievingCookieGenerator;
import lombok.experimental.UtilityClass;
import lombok.val;

/**
 * This is {@link CookieHelper}.
 *
 * @author Misagh Moayyed
 * @since 5.1.0
 */
@UtilityClass
public class CookieHelper {

    /**
     * Build cookie retrieving generator.
     *
     * @param cookie             the cookie
     * @param cookieValueManager the cookie value manager
     * @return the cookie retrieving cookie generator
     */
    public static CasCookieBuilder buildCookieRetrievingGenerator(final CookieProperties cookie,
                                                                  final CookieValueManager cookieValueManager) {
        val context = buildCookieGenerationContext(cookie);
        return buildCookieRetrievingGenerator(cookieValueManager, context);
    }

    /**
     * Build cookie retrieving generator cookie retrieving cookie generator.
     *
     * @param cookieValueManager the cookie value manager
     * @param context            the context
     * @return the cookie retrieving cookie generator
     */
    public static CookieRetrievingCookieGenerator buildCookieRetrievingGenerator(final CookieValueManager cookieValueManager,
                                                                                 final CookieGenerationContext context) {
        return new CookieRetrievingCookieGenerator(context, cookieValueManager);
    }

    /**
     * Build cookie retrieving generator cookie.
     *
     * @param context the context
     * @return the cookie retrieving cookie generator
     */
    public static CookieRetrievingCookieGenerator buildCookieRetrievingGenerator(final CookieGenerationContext context) {
        return buildCookieRetrievingGenerator(CookieValueManager.noOp(), context);
    }

    /**
     * Build cookie retrieving generator.
     *
     * @param cookie the cookie
     * @return the cookie retrieving cookie generator
     */
    public static CasCookieBuilder buildCookieRetrievingGenerator(final CookieProperties cookie) {
        return buildCookieRetrievingGenerator(cookie, CookieValueManager.noOp());
    }

    /**
     * Build cookie generation context.
     *
     * @param cookie the cookie
     * @return the cookie generation context
     */
    public static CookieGenerationContext buildCookieGenerationContext(final CookieProperties cookie) {
        return buildCookieGenerationContextBuilder(cookie).build();
    }

    private static CookieGenerationContext.CookieGenerationContextBuilder buildCookieGenerationContextBuilder(
        final CookieProperties cookie) {

        return CookieGenerationContext.builder()
            .name(cookie.getName())
            .path(cookie.getPath())
            .maxAge(cookie.getMaxAge())
            .secure(cookie.isSecure())
            .domain(cookie.getDomain())
            .sameSitePolicy(cookie.getSameSitePolicy())
            .httpOnly(cookie.isHttpOnly());
    }
}
