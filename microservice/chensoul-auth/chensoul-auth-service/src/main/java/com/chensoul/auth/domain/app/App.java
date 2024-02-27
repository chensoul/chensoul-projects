package com.chensoul.auth.domain.app;

import com.chensoul.mybatis.entity.CommonEntity;
import com.chensoul.validation.Insert;
import com.chensoul.validation.Update;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class App extends CommonEntity<Long> {

    private static final long serialVersionUID = -4721506182563537623L;

    @NotBlank(message = "名称不能为空", groups = {Update.class, Insert.class})
    @Length(max = 16, message = "名称长度不能超过16", groups = {Update.class, Insert.class})
    private String name;

    @Pattern(regexp = "^[a-z\\-]+$", message = "编码只能包括小写字母和横线", groups = {Update.class, Insert.class})
    @Length(max = 32, message = "编码长度不能超过32", groups = {Update.class, Insert.class})
    @NotBlank(message = "编码不能为空", groups = {Update.class, Insert.class})
    private String code;

    private String description;

    private String icon;
}
