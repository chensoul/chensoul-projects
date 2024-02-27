package com.chensoul.mybatis.datascope;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据/租户权限类型
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum {

    /**
     * 自己
     */
    SELF(0, "自己"),
    /**
     * 全部
     */
    ALL(1, "全部"),
    /**
     * 指定
     */
    ASSIGN(2, "指定");

    private static final Map<Integer, DataScopeEnum> CODEMAP = Arrays.stream(DataScopeEnum.class.getEnumConstants())
            .collect(Collectors.toMap(DataScopeEnum::getCode, t -> t));
    private Integer code;
    private String name;

    /**
     * @param code
     * @return
     */
    public static DataScopeEnum fromCode(final Integer code) {
        return CODEMAP.getOrDefault(code, SELF);
    }

}
