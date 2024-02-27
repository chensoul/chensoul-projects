package com.chensoul.spring.util;

import static com.chensoul.constant.StringPool.ASTERISK_TREE;
import com.chensoul.util.InetAddressUtils;
import com.chensoul.util.logging.LoggingUtils;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * This is {@link HttpRequestUtils}.
 */
@UtilityClass
@Slf4j
public class HttpRequestUtils {
    private static final List<String> CLIENT_IP_HEADER_NAMES = Arrays.asList("X-Forwarded-For",
        "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR");

    public static final String USER_AGENT_HEADER = "user-agent";

    private static final int PING_URL_TIMEOUT = 5_000;

    public static Optional<HttpServletRequest> getHttpServletRequest() {
        val requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(requestAttributes).map(ServletRequestAttributes::getRequest);
    }

    /**
     * Gets http servlet response from request attributes.
     *
     * @return the http servlet response from request attributes
     */
    public static Optional<HttpServletResponse> getHttpServletResponse() {
        val requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(requestAttributes).map(ServletRequestAttributes::getResponse);
    }

    /**
     * Gets request headers.
     *
     * @param request the request
     * @return the request headers
     */
    @SuppressWarnings("JdkObsolete")
    public static Map<String, String> getRequestHeaders(final HttpServletRequest request) {
        val headers = new LinkedHashMap<String, Object>();
        if (request != null) {
            val headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    val headerName = headerNames.nextElement();
                    val headerValue = StringUtils.stripToEmpty(request.getHeader(headerName));
                    headers.put(headerName, headerValue);
                }
            }
        }
        return (Map) headers;
    }

    /**
     * Gets http servlet request user agent.
     *
     * @param request the request
     * @return the http servlet request user agent
     */
    public static String getUserAgent(final HttpServletRequest request) {
        if (request != null) {
            return request.getHeader(USER_AGENT_HEADER);
        }
        return null;
    }

    /**
     * Check if a parameter exists.
     *
     * @param request the HTTP request
     * @param name    the parameter name
     * @return whether the parameter exists
     */
    public static boolean doesParameterExist(final HttpServletRequest request, final String name) {
        val parameter = request.getParameter(name);
        if (StringUtils.isBlank(parameter)) {
            log.error("Missing request parameter: [{}]", name);
            return false;
        }
        log.debug("Found provided request parameter [{}]", name);
        return true;
    }

    /**
     * Ping url and return http status.
     *
     * @param location the location
     * @return the http status
     */
    public static HttpStatus pingUrl(final String location) {
        try {
            URL url = new URI(location).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(PING_URL_TIMEOUT);
            connection.setReadTimeout(PING_URL_TIMEOUT);
            connection.setRequestMethod(HttpMethod.HEAD.name());
            return HttpStatus.valueOf(connection.getResponseCode());
        } catch (final Exception e) {
            LoggingUtils.error(log, e);
        }
        return HttpStatus.SERVICE_UNAVAILABLE;

    }

    /**
     * Gets full request url.
     *
     * @param request the request
     * @return the full request url
     */
    public static String getFullRequestUrl(final HttpServletRequest request) {
        return request.getRequestURL() + (request.getQueryString() != null ? '?' + request.getQueryString() : StringUtils.EMPTY);
    }

    /**
     * @param request
     * @return
     */
    public static String createMessage(HttpServletRequest request) {
        return createMessage(request, true, true, true, true, null);
    }

    /**
     * @param request
     * @param headerPredicate
     * @return
     */
    public static String createMessage(HttpServletRequest request, Predicate<String> headerPredicate) {
        return createMessage(request, true, true, true, true, headerPredicate);
    }

    /**
     * @param request
     * @param isIncludeQueryString
     * @param isIncludeClientInfo
     * @param isIncludePayload
     * @param isIncludeHeaders
     * @param headerPredicate
     * @return
     */
    public static String createMessage(HttpServletRequest request, Boolean isIncludeQueryString,
                                       Boolean isIncludeClientInfo, Boolean isIncludePayload, Boolean isIncludeHeaders,
                                       Predicate<String> headerPredicate) {
        StringBuilder msg = new StringBuilder();
        msg.append(request.getMethod()).append(' ');
        msg.append(request.getRequestURL());

        if (isIncludeQueryString) {
            String queryString = request.getQueryString();
            if (queryString != null) {
                msg.append('?').append(queryString);
            }
        }

        if (isIncludeClientInfo) {
            msg.append(", clientIp=").append(getClientIp());
            HttpSession session = request.getSession(false);
            if (session != null) {
                msg.append(", session=").append(session.getId());
            }
            String user = request.getRemoteUser();
            if (user != null) {
                msg.append(", user=").append(user);
            }
        }

        if (isIncludeHeaders) {
            HttpHeaders headers = new ServletServerHttpRequest(request).getHeaders();
            Map<String, String> result = extractedHeaders(headers, headerPredicate);
            msg.append(", headers=").append(result);
        }

        if (isIncludePayload) {
            String payload = getMessagePayload(request);
            if (payload != null) {
                msg.append(", payload=").append(payload);
            }
        }

        return msg.toString();
    }

    /**
     * @param headers
     * @param predicate
     * @return
     */
    public static Map<String, String> extractedHeaders(HttpHeaders headers, Predicate<String> predicate) {
        Map<String, String> result = new HashMap<>();
        headers.toSingleValueMap().forEach((k, v) -> {
            if (predicate != null && predicate.test(k)) {
                result.put(k, ASTERISK_TREE);
            } else {
                result.put(k, v);
            }
        });

        return result;
    }

    /**
     * @param paramMap
     * @param predicate
     * @return
     */
    public static Map<String, Object> extractedParams(Map<String, String[]> paramMap, Predicate<String> predicate) {
        Map<String, Object> result = new HashMap<>();
        paramMap.forEach((k, v) -> {
            if (predicate != null && predicate.test(k)) {
                result.put(k, ASTERISK_TREE);
            } else {
                result.put(k, v.length > 1 ? v : v[0]);
            }
        });
        return result;
    }

    /**
     * @param request
     * @return
     */
    public static String getMessagePayload(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = org.springframework.web.util.WebUtils.getNativeRequest(request,
            ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, 64000);
                return new String(buf, 0, length, Charset.forName(wrapper.getCharacterEncoding()));
            }
        }
        return null;
    }

    /**
     * @param otherHeaderNames
     * @return
     */
    public static String getClientIp(String... otherHeaderNames) {
        return getClientIp(getHttpServletRequest().get(), otherHeaderNames);
    }

    /**
     * @param request
     * @param otherHeaderNames
     * @return
     */
    public static String getClientIp(org.springframework.http.server.reactive.ServerHttpRequest request, String... otherHeaderNames) {
        if (request == null) {
            return null;
        }

        if (ArrayUtils.isNotEmpty(otherHeaderNames)) {
            CLIENT_IP_HEADER_NAMES.addAll(Arrays.asList(otherHeaderNames));
        }

        HttpHeaders httpHeaders = request.getHeaders();

        String ip;
        for (String header : CLIENT_IP_HEADER_NAMES) {
            ip = httpHeaders.getFirst(header);
            if (!InetAddressUtils.isUnknown(ip)) {
                return InetAddressUtils.getReverseProxyIp(ip);
            }
        }

        ip = request.getRemoteAddress().getAddress().getHostAddress();
        return InetAddressUtils.getReverseProxyIp(ip);
    }

    /**
     * @param request
     * @param otherHeaderNames
     * @return
     */
    public static String getClientIp(HttpServletRequest request, String... otherHeaderNames) {
        if (request == null) {
            return null;
        }
        if (ArrayUtils.isNotEmpty(otherHeaderNames)) {
            CLIENT_IP_HEADER_NAMES.addAll(Arrays.asList(otherHeaderNames));
        }

        String ip;
        for (String header : CLIENT_IP_HEADER_NAMES) {
            ip = request.getHeader(header);
            if (!InetAddressUtils.isUnknown(ip)) {
                return InetAddressUtils.getReverseProxyIp(ip);
            }
        }

        ip = request.getRemoteHost();
        return InetAddressUtils.getReverseProxyIp(ip);
    }

    public static String getValueFromRequest(String headerName) {
        return getValueFromRequest(getHttpServletRequest().get(), headerName);
    }

    /**
     * @param request
     * @param headerName
     * @return
     */
    public static String getValueFromRequest(HttpServletRequest request, String headerName) {
        if (request == null) {
            return null;
        }
        String value = request.getParameter(headerName);
        if (StringUtils.isNotBlank(value)) {
            return value;
        } else {
            value = request.getHeader(headerName);
        }
        return value;
    }

}
