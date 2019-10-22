package com.blog.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@TableName(value = "t_blog_tag")
public class Tag implements Serializable {

    @TableId(value = "ID", type = IdType.AUTO)
    private int Id;

    @TableField(value = "tag_name")
    private String tagName;

    @TableField(value = "tag_creation_time")
    private String tagCreationTime;

    @TableField(value = "tag_last_update_time")
    private String tagLastUpdateTime;

    @TableField(value = "is_delete")
    private String isDelete;

}
