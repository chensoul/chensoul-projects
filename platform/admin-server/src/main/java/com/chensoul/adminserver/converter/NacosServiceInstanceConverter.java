package com.chensoul.adminserver.converter;

import de.codecentric.boot.admin.server.cloud.discovery.DefaultServiceInstanceConverter;
import static java.util.Collections.emptyMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 针对 nacos 2.x 服务注册处理
 * </p>
 */
@Configuration(proxyBeanMethods = false)
public class NacosServiceInstanceConverter extends DefaultServiceInstanceConverter {

    @Override
    protected Map<String, String> getMetadata(ServiceInstance instance) {
        // 过滤instanceID为空的属性,要不然nacos服务端查询不到该服务的服务列表信息
        return (instance.getMetadata() != null)
                ? instance.getMetadata().entrySet().stream().filter((e) -> e.getKey() != null && e.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                : emptyMap();
    }

}
