package com.blog.admin.common.controller;
import com.blog.admin.dto.Result;
import com.blog.admin.enums.ResponseStatus;
import com.blog.admin.exception.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * @author liushuai
 */
@RestControllerAdvice
public class GlobalExceptionHandle {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) {
        logger.error(e.getMessage(), e);
        return new Result(ResponseStatus.SYSTEM_ERROR);
    }

    @ExceptionHandler(GlobalException.class)
    public Result globalException(GlobalException e, HttpServletResponse response) {
        logger.error(e.getMsg(), e);
        return new Result(response.getStatus(), e.getMsg());
    }
}
