package com.chensoul.spring.boot.oauth2.util;

import com.chensoul.util.StringUtils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public final class SecurityUtils {
    /**
     *
     */
    private SecurityUtils() {
    }

    public static String getUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return extractPrincipal(securityContext.getAuthentication());
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        if (authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * Checks if the current user has any of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has any of the authorities, false otherwise.
     */
    public static boolean hasAnyOfAuthorities(String... authorities) {
        return getAuthorities().stream().anyMatch(authority -> Arrays.asList(authorities).contains(authority));
    }

    /**
     * Checks if the current user has a specific authority.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static boolean hasAuthority(String authority) {
        return hasAnyOfAuthorities(authority);
    }

    /**
     * @return
     */
    public static Set<String> getAuthorities() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return new HashSet<>();
        }
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户角色信息
     *
     * @return 角色集合
     */
    public static Set<String> getRoles() {
        Authentication authentication = getAuthentication();
        return authentication.getAuthorities().stream()
                .filter(granted -> StringUtils.startsWith(granted.getAuthority(), "ROLE_"))
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    /**
     * @return
     */
    public static String getClientId() {
        Authentication authentication = getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
            return auth2Authentication.getOAuth2Request().getClientId();
        }

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (servletRequestAttributes.getRequest() != null) {
                BasicAuthenticationConverter basicAuthenticationConverter = new BasicAuthenticationConverter();
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = basicAuthenticationConverter.convert(servletRequestAttributes.getRequest());
                if (usernamePasswordAuthenticationToken != null) {
                    return usernamePasswordAuthenticationToken.getName();
                }
            }
        }

        //内部接口没有传递 token 时，header 中传递了客户端ID
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return StringUtils.defaultIfBlank(requestAttributes.getRequest().getParameter("client_id"),
                    requestAttributes.getRequest().getHeader("client_id"));
        }
        return null;
    }
}
