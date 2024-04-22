package com.chensoul.mybatis.spring.boot.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(exclude = {"updater", "update_time", "creator", "create_time"})
public class UpdateEntity extends SaveEntity implements Serializable {

    @TableField(value = "updater", fill = FieldFill.INSERT_UPDATE)
    protected String updater;

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;

}
