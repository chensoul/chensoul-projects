package com.chensoul.auth.domain.account;

import com.chensoul.mybatis.entity.CommonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AccountRole extends CommonEntity<Long> {
    private static final long serialVersionUID = 5598637904531628501L;

    private Long accountId;

    private Long roleId;
}
