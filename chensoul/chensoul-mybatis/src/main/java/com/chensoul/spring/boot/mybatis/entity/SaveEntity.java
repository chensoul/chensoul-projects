package com.chensoul.spring.boot.mybatis.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
public class SaveEntity implements Serializable {
    public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @TableField(value = "creator", fill = FieldFill.INSERT)
    protected String creator;

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

}
