package com.chensoul.auth.infrastructure.security.support;

import com.chensoul.spring.util.HttpRequestUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private static final long serialVersionUID = 4441359628463408329L;
    @Getter
    private final String serverAddress;

    public CustomWebAuthenticationDetails(final HttpServletRequest request, final String serverAddress) {
        super(HttpRequestUtils.getClientIp(request), extractSessionId(request));
        this.serverAddress = serverAddress;
    }

    protected static String extractSessionId(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        return (session != null) ? session.getId() : null;
    }
}
