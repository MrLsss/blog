package com.blog.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "t_blog_classify")
public class Classify implements Serializable {

    @TableId(value = "ID", type = IdType.AUTO)
    private int Id;

    @TableField(value = "classify_name")
    private String classifyName;

    @TableField(value = "classify_dec")
    private String classifyDec;

    @TableField(value = "classify_creation_time")
    private String classifyCreationTime;

    @TableField(value = "classify_last_update_time")
    private String classifyLastUpdateTime;

    @TableField(value = "is_delete")
    private String isDelete;
}
