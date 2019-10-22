package com.blog.admin.dto;


import com.blog.admin.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author liushuai
 * 封装返回结果
 */
@Data
@AllArgsConstructor
public class Result {

    private Integer code;
    private String msg;
    private Object data;

    public Result(ResponseStatus status) {
        this.code = status.getCode();
        this.msg = status.getMsg();
    }

    public Result(ResponseStatus status, Object data) {
        this.code = status.getCode();
        this.msg = status.getMsg();
        this.data = data;
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result SUCCESS() {
        return new Result(ResponseStatus.SUCCESS);
    }

    public static Result SUCCESS(Object data) {
        return new Result(ResponseStatus.SUCCESS, data);
    }

    public static Result ERROR() {
        return new Result(ResponseStatus.SYSTEM_ERROR);
    }

    public static Result ERROR(String msg) {
        return new Result(500, msg);
    }

    public static Result SERVICE_ERROR() {
        return new Result(ResponseStatus.SERVER_ERROR);
    }

    public static Result REQUEST_PARAM_ERROR() {
        return new Result(ResponseStatus.REQUEST_PARAM_ERROR);
    }

}
