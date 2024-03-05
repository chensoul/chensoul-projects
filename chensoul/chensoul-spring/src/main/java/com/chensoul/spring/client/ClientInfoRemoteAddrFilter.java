package com.chensoul.spring.client;

import com.chensoul.lang.function.CheckedConsumer;
import com.chensoul.util.ResultCode;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class ClientInfoRemoteAddrFilter extends OncePerRequestFilter {
    protected Pattern allow = null;
    protected Pattern deny = null;

    @Getter
    @Setter
    protected int denyStatus = 403;

    public String getAllow() {
        return this.allow == null ? null : this.allow.toString();
    }

    public void setAllow(String allow) {
        if (allow != null && allow.length() != 0) {
            this.allow = Pattern.compile(allow);
        } else {
            this.allow = null;
        }
    }

    public String getDeny() {
        return this.deny == null ? null : this.deny.toString();
    }

    public void setDeny(String deny) {
        if (deny != null && deny.length() != 0) {
            this.deny = Pattern.compile(deny);
        } else {
            this.deny = null;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String clientIp = Optional.ofNullable(ClientInfoHolder.getClientInfo())
            .map(ClientInfo::getClientIp)
            .orElseGet(request::getRemoteAddr);
        logger.trace(String.format("Remote address to process is [%s]", clientIp));

        process(clientIp, request, response, filterChain);
    }

    protected void process(String property, HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (this.isAllowed(property)) {
            chain.doFilter(request, response);
            return;
        }

        logger.info(String.format("Remote address [%s] is denied", property));

        response.setStatus(denyStatus);
        this.sendErrorWhenNotHttp(response);
    }

    private boolean isAllowed(String property) {
        if (this.deny != null && this.deny.matcher(property).matches()) {
            return false;
        } else if (this.allow != null && this.allow.matcher(property).matches()) {
            return true;
        } else {
            return this.deny != null && this.allow == null;
        }
    }

    private void sendErrorWhenNotHttp(HttpServletResponse response) throws IOException {
        CheckedConsumer.unchecked(__ -> {
            MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
            jsonConverter.setPrettyPrint(true);
            MediaType jsonMimeType = MediaType.APPLICATION_JSON;

            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);

            jsonConverter.write(ResultCode.FORBIDDEN, jsonMimeType, new ServletServerHttpResponse(response));
        });
    }
}
