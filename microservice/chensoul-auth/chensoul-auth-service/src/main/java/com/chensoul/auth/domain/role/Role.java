package com.chensoul.auth.domain.role;

import com.chensoul.mybatis.spring.boot.entity.CommonEntity;
import com.chensoul.validation.Insert;
import com.chensoul.validation.Update;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Role extends CommonEntity<Long> {
    private static final long serialVersionUID = 788910696102024100L;

    @NotBlank(message = "名称不能为空", groups = {Update.class, Insert.class})
    @Length(max = 8, message = "名称长度不能超过8", groups = {Update.class, Insert.class})
    private String name;

    @Pattern(regexp = "^[a-z\\-]+$", message = "编码只能包括小写字母和横线", groups = {Update.class, Insert.class})
    @Length(max = 32, message = "编码长度不能超过32", groups = {Update.class, Insert.class})
    @NotBlank(message = "编码不能为空", groups = {Update.class, Insert.class})
    private String code;

    private String description;

    @NotNull(message = "应用ID不能为空", groups = {Insert.class})
    private Long appId;

}
