package com.blog.admin.controller;


import com.blog.admin.dto.Result;
import com.blog.admin.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录退出controller
 */
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestParam(value = "account", required = true) String account,
                        @RequestParam(value = "password", required = true) String password,
                        HttpServletRequest request, HttpServletResponse response) throws Exception{
        boolean login = loginService.login(account, password);
        if (login) {
            HttpSession session = request.getSession();
            session.setAttribute("user", account+":"+password);
            session.setMaxInactiveInterval(20*60);
            request.getRequestDispatcher("/admin/index").forward(request, response);
        } else {
            request.getSession().setAttribute("user", null);
            response.sendRedirect(request.getContextPath() + "/admin");
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().setAttribute("user", null);
        response.sendRedirect(request.getContextPath() + "/");
    }

}
