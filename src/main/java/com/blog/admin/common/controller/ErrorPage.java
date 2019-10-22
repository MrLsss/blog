package com.blog.admin.common.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorPage implements ErrorController {


    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        switch (statusCode) {
            case 404:
                return "admin/common/404";
            case 500:
                return "admin/common/500";
            default:
            return "null";
        }
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
