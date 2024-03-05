package com.chensoul.auth.domain.thirdapp;

import com.chensoul.spring.boot.mybatis.entity.CommonEntity;
import com.chensoul.validation.Insert;
import com.chensoul.validation.Update;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ThirdApp extends CommonEntity<Long> {

    private static final long serialVersionUID = -4721506182563537623L;

    @NotBlank(message = "名称不能为空", groups = {Update.class, Insert.class})
    @Length(max = 16, message = "名称长度不能超过16", groups = {Update.class, Insert.class})
    private String name;

    private String description;

    private String icon;

    private String clientId;

    private String clientSecret;


}
