package com.chensoul.auth.domain.role;

import com.chensoul.spring.boot.mybatis.entity.CommonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RoleResource extends CommonEntity<Long> {

    private static final long serialVersionUID = 8580387047728477695L;

    private Long roleId;

    private Long resourceId;
}
