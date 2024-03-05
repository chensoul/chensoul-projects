package com.chensoul.auth.domain.tenant;

import com.chensoul.spring.boot.mybatis.entity.CommonEntity;
import com.chensoul.validation.Insert;
import com.chensoul.validation.Update;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Tenant extends CommonEntity<Long> {
    private static final long serialVersionUID = 5651059970117055491L;

    @NotBlank(message = "名称不能为空", groups = {Update.class, Insert.class})
    @Length(max = 16, message = "名称长度不能超过16", groups = {Update.class, Insert.class})
    private String name;

    @Pattern(regexp = "^[a-z\\-]+$", message = "编码只能包括小写字母和横线", groups = {Update.class, Insert.class})
    @Length(max = 32, message = "编码长度不能超过32", groups = {Update.class, Insert.class})
    @NotBlank(message = "编码不能为空", groups = {Update.class, Insert.class})
    private String code;

    private Long appId;

    private String extraInfo;

}
