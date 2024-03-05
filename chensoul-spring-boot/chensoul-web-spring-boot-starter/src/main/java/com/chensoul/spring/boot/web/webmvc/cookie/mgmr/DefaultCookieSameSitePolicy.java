package com.chensoul.spring.boot.web.webmvc.cookie.mgmr;

import com.chensoul.spring.boot.web.webmvc.cookie.CookieGenerationContext;
import com.chensoul.spring.boot.web.webmvc.cookie.CookieSameSitePolicy;
import com.chensoul.groovy.scripting.WatchableGroovyScript;
import com.chensoul.spring.util.ResourceUtils;
import com.chensoul.util.function.CheckedSupplier;
import com.chensoul.util.function.FunctionUtils;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.ClassUtils;

/**
 * This is {@link DefaultCookieSameSitePolicy}.
 *
 * @author Misagh Moayyed
 * @since 7.0.0
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class DefaultCookieSameSitePolicy implements CookieSameSitePolicy {
    /**
     * Policy instance.
     */
    public static final CookieSameSitePolicy INSTANCE = new DefaultCookieSameSitePolicy();

    @Override
    public Optional<String> build(final HttpServletRequest request, final HttpServletResponse response,
                                  final CookieGenerationContext cookieGenerationContext) {
        val sameSitePolicy = cookieGenerationContext.getSameSitePolicy();
        if (ResourceUtils.doesResourceExist(sameSitePolicy)) {
            return buildSameSitePolicyFromScript(request, response, cookieGenerationContext);
        }
        if (sameSitePolicy.contains(".")) {
            return CheckedSupplier.unchecked(() -> {
                val clazz = ClassUtils.getClass(CookieSameSitePolicy.class.getClassLoader(), sameSitePolicy);
                return (CookieSameSitePolicy) clazz.getDeclaredConstructor().newInstance();
            }).get().build(request, response, cookieGenerationContext);
        }
        final CookieSameSitePolicy result;
        switch (sameSitePolicy.toLowerCase(Locale.ENGLISH).trim()) {
            case "strict":
                result = CookieSameSitePolicy.strict();
                break;
            case "lax":
                result = CookieSameSitePolicy.lax();
                break;
            case "off":
                result = CookieSameSitePolicy.off();
                break;
            default:
                result = CookieSameSitePolicy.none();
                break;
        }
        return result.build(request, response, cookieGenerationContext);
    }

    protected static Optional<String> buildSameSitePolicyFromScript(final HttpServletRequest request, final HttpServletResponse response,
                                                                    final CookieGenerationContext cookieGenerationContext) {
        return FunctionUtils.doUnchecked(() -> {
            val sameSitePolicy = cookieGenerationContext.getSameSitePolicy();
            val resource = ResourceUtils.getResourceFrom(sameSitePolicy);
            try (val groovyResource = new WatchableGroovyScript(resource, false)) {
                return Optional.ofNullable(groovyResource.execute(
                        new Object[]{request, response, cookieGenerationContext, log}, String.class));
            }
        });
    }
}
