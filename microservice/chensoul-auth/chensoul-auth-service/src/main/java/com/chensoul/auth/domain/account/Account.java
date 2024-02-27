package com.chensoul.auth.domain.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Account extends CommonEntity<Long> {
    private static final long serialVersionUID = 8650576281085920404L;
    private String username;

    private String nickname;

    private String realname;

    private String gender;

    @JsonIgnore
    private String password;

    private String phone;

    private String email;

    private String idCard;

    private String avatar;

    private String openId;

}
