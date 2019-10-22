package com.blog.admin.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "t_blog_webmasterinfo")
public class WebMasterInfo implements Serializable {

    @TableId(type = IdType.AUTO)
    private int Id;

    @TableField(value = "wm_name")
    private String wmName;

    @TableField(value = "wm_email")
    private String wmEmail;

    @TableField(value = "wm_weixin")
    private String wmWeixin;

    @TableField(value = "wm_alipay")
    private String wmAlipay;

    @TableField(value = "wm_qq")
    private String wmQq;

    @TableField(value = "wm_github")
    private String wmGithub;

    @TableField(value = "wm_account")
    private String wmAccount;

    @TableField(value = "wm_password")
    private String wmPassword;

    @TableField(value = "wm_head_img")
    private String wmHeadImg;

    @TableField(value = "wm_dec")
    private String wmDec;

}
