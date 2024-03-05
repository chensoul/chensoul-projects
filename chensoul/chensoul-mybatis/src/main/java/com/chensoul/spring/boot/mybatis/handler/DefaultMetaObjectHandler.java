package com.chensoul.spring.boot.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.chensoul.spring.boot.mybatis.tenant.TenantContextHolder;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ClassUtils;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class DefaultMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();

        fillValIfNullByName("isDelete", false, metaObject, true);
        fillValIfNullByName("status", true, metaObject, true);
        fillValIfNullByName("createdTime", now, metaObject, false);
        fillValIfNullByName("updatedTime", now, metaObject, false);
        fillValIfNullByName("createdBy", getUsername(), metaObject, false);
        fillValIfNullByName("updatedBy", getUsername(), metaObject, false);

        fillValIfNullByName("createTime", now, metaObject, false);
        fillValIfNullByName("updateTime", now, metaObject, false);
        fillValIfNullByName("creator", getUsername(), metaObject, false);
        fillValIfNullByName("updater", getUsername(), metaObject, false);

        fillValIfNullByName("tenantId", TenantContextHolder.getTenantId(), metaObject, false);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        fillValIfNullByName("updatedTime", LocalDateTime.now(), metaObject, true);
        fillValIfNullByName("updatedBy", getUsername(), metaObject, true);
        fillValIfNullByName("createTime", LocalDateTime.now(), metaObject, true);
        fillValIfNullByName("updateTime", getUsername(), metaObject, true);
    }

    /**
     * @return
     */
    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

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
     * 填充值，先判断是否有手动设置，优先手动设置的值，例如：job必须手动设置
     *
     * @param fieldName  属性名
     * @param fieldVal   属性值
     * @param metaObject MetaObject
     * @param isCover    是否覆盖原有值,避免更新操作手动入参
     */
    protected static void fillValIfNullByName(String fieldName, Object fieldVal, MetaObject metaObject,
                                              boolean isCover) {
        // 1. 没有 set 方法
        if (!metaObject.hasSetter(fieldName)) {
            return;
        }
        // 2. 如果用户有手动设置的值
        if (metaObject.getValue(fieldName) != null && !isCover) {
            return;
        }
        // 3. 类型相同时设置
        Class<?> getterType = metaObject.getGetterType(fieldName);
        if (ClassUtils.isAssignableValue(getterType, fieldVal)) {
            metaObject.setValue(fieldName, fieldVal);
        }
    }
}
