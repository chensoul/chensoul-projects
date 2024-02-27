package com.chensoul.auth.domain.account;

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
public class AccountAppAuth {
    private Long accountId;

    private Long appId;

    /**
     * phone、email、username、qq、wechat、dingtalk
     */
    private Integer authType;

    private String principal;

    private String credential;

    private String extraInfo;


}
