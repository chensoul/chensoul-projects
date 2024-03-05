package com.chensoul.auth.domain.client;

import com.chensoul.spring.boot.mybatis.entity.CommonEntity;
import com.chensoul.validation.Insert;
import com.chensoul.validation.Update;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Client extends CommonEntity<Long> {

    private static final long serialVersionUID = -2188203188984155658L;

    @Pattern(regexp = "^[a-z\\-]+$", message = "编码只能包括小写字母和横线", groups = {Update.class, Insert.class})
    @Length(max = 32, message = "编码长度不能超过32", groups = {Update.class, Insert.class})
    @NotBlank(message = "客户端不能为空", groups = {Update.class, Insert.class})
    private String code;

    private String resourceIds;

    @NotBlank(message = "密钥不能为空", groups = {Update.class, Insert.class})
    private String secret;

    @NotBlank(message = "作用域不能为空", groups = {Update.class, Insert.class})
    private String scope;

    @NotBlank(message = "授权方式不能为空", groups = {Update.class, Insert.class})
    private String authorizedGrantTypes;

    private String webServerRedirectUri;

    private String authorities;

    @NotNull(message = "请求令牌有效时间不能为空", groups = {Update.class, Insert.class})
    private Integer accessTokenValidity;

    @NotNull(message = "刷新令牌有效时间不能为空", groups = {Update.class, Insert.class})
    private Integer refreshTokenValidity;

    private String additionalInformation;

    @NotBlank(message = "是否自动授权不能为空", groups = {Update.class, Insert.class})
    private String autoapprove;
}
