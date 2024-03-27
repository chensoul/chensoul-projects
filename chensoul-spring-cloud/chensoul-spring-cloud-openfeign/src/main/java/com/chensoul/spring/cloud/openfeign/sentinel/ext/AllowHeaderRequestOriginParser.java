
package com.chensoul.spring.cloud.openfeign.sentinel.ext;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import javax.servlet.http.HttpServletRequest;

/**
 * sentinel 请求头解析判断
 */
public class AllowHeaderRequestOriginParser implements RequestOriginParser {

    private static final String ALLOW = "Allow";

    /**
     * Parse the origin from given HTTP request.
     *
     * @param request HTTP request
     * @return parsed origin
     */
    @Override
    public String parseOrigin(final HttpServletRequest request) {
        return request.getHeader(ALLOW);
    }

}
