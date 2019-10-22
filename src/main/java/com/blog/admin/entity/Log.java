package com.blog.admin.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "t_blog_log")
public class Log implements Serializable {

    @TableId(value = "ID", type = IdType.AUTO)
    private int Id;

    @TableField(value = "log_user")
    private String logUser;

    @TableField(value = "log_dec")
    private String logDec;

    @TableField(value = "log_require_time")
    private String logRequireTime;

    @TableField(value = "log_method")
    private String logMethod;

    @TableField(value = "log_param")
    private String logParam;

    @TableField(value = "log_ip")
    private String logIp;

    @TableField(value = "log_time")
    private String logTime;
}
