package com.chensoul.spring.boot.oauth2.support;

import com.chensoul.spring.boot.oauth2.util.SecurityUtils;
import com.chensoul.util.StringUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

/**
 * 接口权限判断工具
 */
@Slf4j
public class PermissionService {

    private static final String COMMA = ",";
    private static final String ALL_PERMISSION = "*:*:*";

    /**
     * 判断接口是否有xxx:xxx权限
     *
     * @param permissions 权限
     * @return {boolean}
     */
    public boolean hasPermission(String permissions) {
        if (StringUtils.isBlank(permissions)) {
            return true;
        }
        Set<String> userPermissions = SecurityUtils.getAuthorities();
        return Arrays.stream(permissions.split(COMMA)).filter(StringUtils::isNotBlank).allMatch(x -> hasPermissions(userPermissions, x));
    }

    /**
     * 验证用户是否不具备某权限，与 hasPermission 逻辑相反
     *
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
    public boolean lacksPermission(String permission) {
        return !hasPermission(permission);
    }

    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param permissions 以 DELIMITER 为分隔符的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermission(String permissions) {
        if (StringUtils.isBlank(permissions)) {
            return true;
        }
        Collection<String> userPermissions = SecurityUtils.getAuthorities();
        return Arrays.stream(permissions.split(COMMA)).filter(StringUtils::isNotBlank).anyMatch(x -> hasPermissions(userPermissions, x));
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Collection<String> authorities, String permission) {
        return authorities.stream().anyMatch(x -> ALL_PERMISSION.equals(x) || PatternMatchUtils.simpleMatch(permission, x));
    }

}
