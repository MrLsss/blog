package com.blog.admin.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 响应状态码
 */
public enum  ResponseStatus {

    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(500, "系统异常"),
    REQUEST_PARAM_ERROR(500, "请求参数错误"),
    SERVER_ERROR(500, "业务验证错误");

    @Getter
    @Setter
    private int code;
    @Getter
    @Setter
    private String msg;

    ResponseStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
