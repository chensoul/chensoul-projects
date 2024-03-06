package com.chensoul.spring.boot.web.webmvc.cookie;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is {@link CookieSameSitePolicy}.
 */
@FunctionalInterface
public interface CookieSameSitePolicy {

    /**
     * None cookie same site policy.
     *
     * @return the cookie same site policy
     */
    static CookieSameSitePolicy none() {
        return (request, response, __) -> Optional.of("SameSite=None;");
    }

    /**
     * Lax cookie same site policy.
     *
     * @return the cookie same site policy
     */
    static CookieSameSitePolicy lax() {
        return (request, response, __) -> Optional.of("SameSite=Lax;");
    }

    /**
     * Strict cookie same site policy.
     *
     * @return the cookie same site policy
     */
    static CookieSameSitePolicy strict() {
        return (request, response, __) -> Optional.of("SameSite=Strict;");
    }

    /**
     * Off cookie same site policy.
     *
     * @return the cookie same site policy
     */
    static CookieSameSitePolicy off() {
        return (request, response, __) -> Optional.empty();
    }

    /**
     * Build option string based on the same-site option type.
     *
     * @param request                 the request
     * @param response                the response
     * @param cookieGenerationContext the cookie generation context
     * @return the string
     */
    Optional<String> build(HttpServletRequest request, HttpServletResponse response, CookieGenerationContext cookieGenerationContext);
}