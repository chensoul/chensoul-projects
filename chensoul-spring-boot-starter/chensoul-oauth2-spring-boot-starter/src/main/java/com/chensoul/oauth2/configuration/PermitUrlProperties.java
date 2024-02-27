package com.chensoul.oauth2.configuration;

import com.chensoul.oauth2.Inner;
import com.chensoul.spring.support.SpringContextHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 资源服务器对外直接暴露URL,如果设置contex-path 要特殊处理
 */
@Slf4j
@ConfigurationProperties(prefix = "security.oauth2.client")
public class PermitUrlProperties implements InitializingBean {
    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");
    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    /**
     * 免认证资源路径，支持通配符
     */
    @Setter
    @Getter
    private final List<String> ignoreUrls = new ArrayList<>();

    @Getter
    private final List<Url> innerUrls = new ArrayList<>();

    /**
     * 必须认证资源路径，支持通配符
     */
    @Getter
    private final List<Url> authenticatedUrls = new ArrayList<>();

    @Override
    public void afterPropertiesSet() {
        final RequestMappingHandlerMapping mapping = SpringContextHolder.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        final Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        final List<Url> temp = new ArrayList<>();

        for (final RequestMappingInfo info : map.keySet()) {
            final HandlerMethod handlerMethod = map.get(info);
            // 获取方法上边的注解 替代path variable 为 *
            final Inner method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Inner.class);
            this.addInnerUrls(info, method, this.innerUrls);

            // 获取类上边的注解, 替代path variable 为 *
            final Inner controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Inner.class);
            this.addInnerUrls(info, controller, this.innerUrls);

            if (Objects.isNull(method) && Objects.isNull(controller) && info.getPathPatternsCondition() != null) {
                info.getPathPatternsCondition().getPatterns()
                    .forEach(pathPattern -> info.getMethodsCondition().getMethods()
                        .forEach(requestMethod -> temp.add(
                            new Url().setMethod(HttpMethod.resolve(requestMethod.name())).setUrl(RegExUtils.replaceAll(pathPattern.getPatternString(), PATTERN, "*")))));
            }
        }

        temp.forEach(t -> {
            if (!this.innerUrls.stream().anyMatch(innerUrl -> innerUrl.method.equals(t.method) && MATCHER.match(innerUrl.url, t.url))) {
                this.authenticatedUrls.add(t);
            }
        });
        log.info("authenticatedUrls: {}", this.authenticatedUrls);
    }

    private void addInnerUrls(final RequestMappingInfo info, final Inner inner, final List<Url> innerUrls) {
        Optional.ofNullable(inner).ifPresent(
            in -> {
                if (info.getPatternsCondition() != null) {
                    info.getPatternsCondition().getPatterns().forEach(url -> info.getMethodsCondition().getMethods()
                        .forEach(requestMethod -> innerUrls.add(new Url().setMethod(HttpMethod.resolve(requestMethod.name()))
                            .setUrl(RegExUtils.replaceAll(url, PATTERN, "*")))
                        ));
                }
            }
        );
    }

    /**
     *
     */
    @Data
    @Accessors(chain = true)
    public class Url {
        private HttpMethod method;
        private String url;
    }
}
