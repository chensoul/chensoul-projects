package com.chensoul.auth.domain.resource;

import com.chensoul.spring.boot.mybatis.entity.CommonEntity;
import com.chensoul.validation.Insert;
import com.chensoul.validation.Update;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Resource extends CommonEntity {

    private static final long serialVersionUID = -7559251893834280905L;

    @NotNull(message = "ID不能为空", groups = {Update.class})
    private Long id;

    @NotBlank(message = "名称不能为空", groups = {Insert.class, Update.class})
    @Length(max = 32, message = "名称长度不能超过32", groups = {Insert.class, Update.class})
    private String name;

    //@ApiModelProperty(value = "类型：1模块,2菜单,3链接,4按钮")
    @NotNull(message = "类型不能为空", groups = {Insert.class, Update.class})
    private Integer type;

    private String icon;

    @Length(max = 512, message = "路由长度不能超过512", groups = {Insert.class, Update.class})
    private String router;

    @Length(max = 256, message = "权限标识长度不能超过256", groups = {Insert.class, Update.class})
    private String permission;

    private String description;

    @NotNull(message = "应用ID不能为空", groups = {Insert.class})
    private Long appId;

    private Long parentId;

    private Integer orderNum;

}
