package com.blog.admin.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "t_blog_siteinfo")
public class SiteInfo implements Serializable {

    @TableId(value = "ID", type = IdType.AUTO)
    private int Id;

    @TableField(value = "site_name")
    private String siteName;

    @TableField(value = "site_dec")
    private String siteDec;

    @TableField(value = "site_blog_address")
    private String siteBlogAddress;

    @TableField(value = "site_address")
    private String siteAddress;

    @TableField(value = "site_img")
    private String siteImg;

    @TableField(value = "site_logo")
    private String siteLogo;

    @TableField(value = "site_copyright")
    private String siteCopyright;

    @TableField(value = "site_number")
    private String siteNumber;

}
