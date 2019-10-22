package com.blog.admin.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "t_blog_link")
public class Link implements Serializable {

    @TableId(value = "ID", type = IdType.AUTO)
    private int Id;

    @TableField(value = "link_url")
    private String linkUrl;

    @TableField(value = "link_name")
    private String linkName;

    @TableField(value = "link_dec")
    private String linkDec;

    @TableField(value = "link_creation_time")
    private String linkCreationTime;

    @TableField(value = "is_delete")
    private String isDelete;
}
