package com.chensoul.spring.boot.web.webmvc.cookie.gen;

import com.chensoul.spring.boot.web.webmvc.cookie.CasCookieBuilder;
import com.chensoul.spring.boot.web.webmvc.cookie.CookieGenerationContext;
import com.chensoul.spring.boot.web.webmvc.cookie.CookieValueManager;
import com.chensoul.spring.boot.web.webmvc.cookie.InvalidCookieException;
import com.chensoul.util.function.FunctionUtils;
import com.chensoul.util.logging.LoggingUtils;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.CookieGenerator;

/**
 * Extends CookieGenerator to allow you to retrieve a value from a request.
 * The cookie is automatically marked as httpOnly, if the servlet container has resource for it.
 * Also has resource for remember-me.
 *
 * @author Scott Battaglia
 * @author Misagh Moayyed
 * @since 3.1
 */
@Slf4j
@Getter
public class CookieRetrievingCookieGenerator extends CookieGenerator implements Serializable, CasCookieBuilder {
    private static final long serialVersionUID = -4926982428809856313L;

    private final CookieValueManager casCookieValueManager;

    private final CookieGenerationContext cookieGenerationContext;

    public CookieRetrievingCookieGenerator(final CookieGenerationContext context) {
        this(context, CookieValueManager.noOp());
    }

    public CookieRetrievingCookieGenerator(final CookieGenerationContext context,
                                           final CookieValueManager casCookieValueManager) {
        super.setCookieName(context.getName());
        super.setCookiePath(context.getPath());
        super.setCookieMaxAge(context.getMaxAge());
        super.setCookieSecure(context.isSecure());
        super.setCookieHttpOnly(context.isHttpOnly());
        this.setCookieDomain(context.getDomain());

        this.cookieGenerationContext = context;
        this.casCookieValueManager = casCookieValueManager;
    }

    @Override
    public void setCookieDomain(final String cookieDomain) {
        super.setCookieDomain(StringUtils.defaultIfEmpty(cookieDomain, null));
    }

    @Override
    protected Cookie createCookie(@NonNull final String cookieValue) {
        val cookie = super.createCookie(cookieValue);
        cookie.setPath(this.cleanCookiePath(cookie.getPath()));
        return cookie;
    }

    @Override
    public Cookie addCookie(final HttpServletRequest request, final HttpServletResponse response,
                            final boolean rememberMe, final String cookieValue) {
        val theCookieValue = this.casCookieValueManager.buildCookieValue(cookieValue, request);
        val cookie = this.createCookie(theCookieValue);

        if (rememberMe) {
            log.trace("Creating CAS cookie [{}] for remember-me authentication", this.getCookieName());
            cookie.setMaxAge(this.cookieGenerationContext.getRememberMeMaxAge());
        } else {
            log.trace("Creating CAS cookie [{}]", this.getCookieName());
            if (this.getCookieMaxAge() != null) {
                cookie.setMaxAge(this.getCookieMaxAge());
            }
        }
        cookie.setSecure(this.isCookieSecure());
        cookie.setHttpOnly(this.isCookieHttpOnly());

        return this.addCookieHeaderToResponse(cookie, request, response);
    }

    @Override
    public Cookie addCookie(final HttpServletRequest request, final HttpServletResponse response, final String cookieValue) {
        return this.addCookie(request, response, false, cookieValue);
    }

    @Override
    public String retrieveCookieValue(final HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(this.getCookieName())) {
                throw new InvalidCookieException("Cookie name is undefined");
            }
            Cookie cookie = org.springframework.web.util.WebUtils.getCookie(request, Objects.requireNonNull(this.getCookieName()));
            if (cookie == null) {
                val cookieValue = request.getHeader(this.getCookieName());
                if (StringUtils.isNotBlank(cookieValue)) {
                    log.trace("Found cookie [{}] under header name [{}]", cookieValue, this.getCookieName());
                    cookie = this.createCookie(cookieValue);
                }
            }
            if (cookie == null) {
                val cookieValue = request.getParameter(this.getCookieName());
                if (StringUtils.isNotBlank(cookieValue)) {
                    log.trace("Found cookie [{}] under request parameter name [{}]", cookieValue, this.getCookieName());
                    cookie = this.createCookie(cookieValue);
                }
            }
            return Optional.ofNullable(cookie)
                .map(ck -> this.casCookieValueManager.obtainCookieValue(ck, request))
                .orElse(null);
        } catch (final Exception e) {
            LoggingUtils.warn(log, e);
        }
        return null;
    }

    @Override
    public void removeAll(final HttpServletRequest request, final HttpServletResponse response) {
        Optional.ofNullable(request.getCookies()).ifPresent(cookies -> Arrays.stream(cookies)
            .filter(cookie -> StringUtils.equalsIgnoreCase(cookie.getName(), this.getCookieName()))
            .forEach(cookie ->
                Stream
                    .of("/", this.getCookiePath(),
                        StringUtils.removeEndIgnoreCase(this.getCookiePath(), "/"),
                        StringUtils.appendIfMissing(this.getCookiePath(), "/"))
                    .distinct()
                    .filter(StringUtils::isNotBlank)
                    .forEach(path -> {
                        val crm = new Cookie(cookie.getName(), removeSpecial(cookie.getValue()));
                        crm.setMaxAge(0);
                        crm.setPath(path);
                        crm.setSecure(cookie.getSecure());
                        crm.setHttpOnly(cookie.isHttpOnly());
                        log.debug("Removing cookie [{}] with path [{}] and [{}]", crm.getName(), crm.getPath(), crm.getValue());
                        response.addCookie(crm);
                    })));
    }

    private static String removeSpecial(String str) {
        return str.replaceAll("[^a-zA-Z ]", "");
    }

    protected Cookie addCookieHeaderToResponse(final Cookie cookie,
                                               final HttpServletRequest request,
                                               final HttpServletResponse response) {
        val builder = new StringBuilder();
        builder.append(String.format("%s=%s;", cookie.getName(), cookie.getValue()));

        if (cookie.getMaxAge() > -1) {
            builder.append(String.format(" Max-Age=%s;", cookie.getMaxAge()));
        }
        if (StringUtils.isNotBlank(cookie.getDomain())) {
            builder.append(String.format(" Domain=%s;", cookie.getDomain()));
        }
        val path = this.cleanCookiePath(cookie.getPath());
        builder.append(String.format(" Path=%s;", path));

        val sameSitePolicy = this.casCookieValueManager.getCookieSameSitePolicy();
        val sameSiteResult = sameSitePolicy.build(request, response, this.cookieGenerationContext);
        sameSiteResult.ifPresent(result -> builder.append(String.format(" %s", result)));
        if (cookie.getSecure() || (sameSiteResult.isPresent() && StringUtils.equalsIgnoreCase(sameSiteResult.get(), "none"))) {
            builder.append(" Secure;");
            log.trace("Marked cookie [{}] as secure as indicated by cookie oauth2 or the configured same-site policy", cookie.getName());
        }
        if (cookie.isHttpOnly()) {
            builder.append(" HttpOnly;");
        }
        val value = StringUtils.removeEndIgnoreCase(builder.toString(), ";");
        log.trace("Adding cookie header as [{}]", value);
        val setCookieHeaders = response.getHeaders("Set-Cookie");
        response.setHeader("Set-Cookie", value);
        setCookieHeaders.stream()
            .filter(header -> !header.startsWith(cookie.getName() + '='))
            .forEach(header -> response.addHeader("Set-Cookie", header));
        return cookie;
    }

    private String cleanCookiePath(final String givenPath) {
        return FunctionUtils.doIf(StringUtils.isBlank(this.cookieGenerationContext.getPath()),
            () -> {
                val path = StringUtils.removeEndIgnoreCase(StringUtils.defaultIfBlank(givenPath, CookieGenerator.DEFAULT_COOKIE_PATH), "/");
                return StringUtils.defaultIfBlank(path, "/");
            },
            () -> StringUtils.defaultIfBlank(givenPath, CookieGenerator.DEFAULT_COOKIE_PATH)).get();
    }
}
