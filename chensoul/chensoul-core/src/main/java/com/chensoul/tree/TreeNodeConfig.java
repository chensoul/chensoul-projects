package com.chensoul.tree;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class TreeNodeConfig implements Serializable {
    private static final long serialVersionUID = 3653051391609093595L;
    public static TreeNodeConfig treeNodeConfig = new TreeNodeConfig();

    private String idKey = "id";

    private String parentIdKey = "parentId";

    private String weightKey = "weight";

    private String nameKey = "name";

    private String childrenKey = "children";

    private Boolean reversed = false;

    // 可以配置递归深度 从0开始计算 默认此配置为空,即不限制
    private Integer deep;

}
