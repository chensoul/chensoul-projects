package com.chensoul.mybatis.spring.boot.datascope;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class DataColumnProperty {

    private String alias;

    private String name;

    /**
     * @return
     */
    public String getAliasDotName() {
        return StringUtils.isBlank(this.alias) ? this.name : this.alias + "." + this.name;
    }

}
