package com.chensoul.spring.client;

import static com.chensoul.constant.StringPool.EMPTY;

import com.chensoul.lang.function.CheckedSupplier;
import com.chensoul.spring.util.HttpRequestUtils;
import com.chensoul.util.StringUtils;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.util.HtmlUtils;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
public class ClientInfo implements Serializable {
    private static final long serialVersionUID = 7492721606084356617L;

    private static final String UNKNOWN = "unknown";

    private Map<String, Serializable> extraInfo = new HashMap<>();

    private Map<String, String> headers = new HashMap<>();

    private String clientIp = UNKNOWN;

    private String serverIp = UNKNOWN;

    private String userAgent = EMPTY;

    private Locale locale = Locale.getDefault();

    public ClientInfo(final String clientIp, final String serverIp, final String userAgent) {
        this.setClientIp(clientIp);
        this.setServerIp(serverIp);
        this.setUserAgent(userAgent);
    }

    public static ClientInfo empty() {
        return new ClientInfo();
    }

    public static ClientInfo from(final HttpServletRequest request) {
        return ClientInfo.from(request, ClientInfoOptions.builder().build());
    }

    public static ClientInfo from(final HttpServletRequest request, final ClientInfoOptions options) {
        final Locale locale = request != null ? request.getLocale() : Locale.getDefault();
        String serverIp = null, clientIp = null, userAgent = null;
        final Map<String, String> headers = new HashMap();

        if (request != null) {
            serverIp = request.getLocalAddr();
            clientIp = request.getRemoteAddr();
            userAgent = HttpRequestUtils.getUserAgent(request);

            if (options != null) {
                if (options.isUseServerHostAddress()) {
                    serverIp = CheckedSupplier.unchecked(() -> InetAddress.getLocalHost().getHostAddress()).get();
                } else if (options.getServerIpHeaderName() != null && !options.getServerIpHeaderName().isEmpty()) {
                    serverIp = StringUtils.defaultIfBlank(request.getHeader(options.getServerIpHeaderName()), request.getLocalAddr());
                }

                if (options.getClientIpHeaderName() != null && !options.getClientIpHeaderName().isEmpty()) {
                    clientIp = StringUtils.defaultIfBlank(request.getHeader(options.getClientIpHeaderName()), request.getRemoteAddr());
                }

                final List<String> definedHeaders = options.getSupportedHeaders();
                Collections.list(request.getHeaderNames())
                    .stream()
                    .filter(h -> definedHeaders.contains("*") || definedHeaders.contains(h))
                    .forEach(h -> headers.put(h, request.getHeader(h)));
            } else {
                headers.putAll(HttpRequestUtils.getRequestHeaders(request));
            }
        }

        serverIp = serverIp == null ? UNKNOWN : serverIp;
        clientIp = clientIp == null ? UNKNOWN : clientIp;
        userAgent = userAgent == null ? UNKNOWN : userAgent;

        return ClientInfo
            .empty()
            .setClientIp(clientIp)
            .setServerIp(serverIp)
            .setLocale(locale)
            .setUserAgent(HtmlUtils.htmlEscape(userAgent))
            .setHeaders(headers);
    }

    public ClientInfo include(final String name, final Serializable value) {
        this.extraInfo.put(name, value);
        return this;
    }
}
