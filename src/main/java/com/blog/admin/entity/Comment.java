package com.blog.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "t_blog_comment")
public class Comment implements Serializable {

    @TableId(value = "ID")
    private String Id;

    @TableField(value = "comment_nickname")
    private String commentNickname;

    @TableField(value = "comment_content")
    private String commentContent;

    @TableField(value = "comment_ip")
    private String commentIp;

    @TableField(value = "comment_address")
    private String commentAddress;

    @TableField(value = "comment_email")
    private String commentEmail;

    @TableField(value = "comment_time")
    private String commentTime;

    //1=留言板，2=文章
    @TableField(value = "comment_from")
    private String commentFrom;

    @TableField(value = "comment_for")
    private String commentFor;

    private String commentForTitle;

    @TableField(value = "comment_father")
    private String commentFather;

    private String commentFatherNickname;

    private String commentFatherContent;

    @TableField(value = "comment_status")
    private String commentStatus;

    @TableField(value = "comment_remark")
    private String commentRemark;

    @TableField(value = "is_delete")
    private String isDelete;
}
