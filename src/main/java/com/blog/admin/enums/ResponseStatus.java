package com.blog.admin.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 响应状态码
 */
public enum  ResponseStatus {

    /*成功状态码*/
    SUCCESS(200, "成功！", true),
    /*系统错误（通用错误）*/
    SYSTEM_ERROR(500, "程序异常", false),
    /*参数错误:1001-1999*/
    PARAMS_IS_INVALID(1001, "参数无效", false),
    PARAMS_IS_EMPTY(1002, "参数为空", false),
    PARAMS_NOT_COMPLETE(1003, "参数缺失", false),
    PARAMS_TYPE_BIND_ERROR(1004, "参数类型错误", false),
    /*用户错误2001-2999*/
    USER_NOT_LOGIN(2001, "用户未登录，请先登录", false),
    USER_NOT_EXIST(2002, "账号不存在", false),
    USER_PASSWORD_ERROR(2003, "密码错误", false),
    USER_HAS_EXIST(2004, "账号已存在", false);

    @Getter
    @Setter
    private Integer code;
    @Getter
    @Setter
    private String msg;
    @Getter
    @Setter
    private Boolean success;

    ResponseStatus(Integer code, String msg, Boolean success) {
        this.code = code;
        this.msg = msg;
        this.success = success;
    }
}
