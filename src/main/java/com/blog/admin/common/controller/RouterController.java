package com.blog.admin.common.controller;


import com.blog.admin.config.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liushuai
 * 后台路由
 */
@Controller
@RequestMapping("/admin")
public class RouterController {

    @RequestMapping({"/", ""})
    public String login() {
        return "admin/login";
    }

    @LoginRequired
    @RequestMapping("/index")
    public String index() {
        return "admin/index";
    }

    @LoginRequired
    @RequestMapping("/article")
    public String article() {
        return "admin/pages/article";
    }

    @LoginRequired
    @RequestMapping("/classify")
    public String classify() {
        return "admin/pages/classify";
    }

    @LoginRequired
    @RequestMapping("/tag")
    public String tag() {
        return "admin/pages/tag";
    }

    @LoginRequired
    @RequestMapping("/comment")
    public String comment() {
        return "admin/pages/comment";
    }

    @LoginRequired
    @RequestMapping("/link")
    public String link() {
        return "admin/pages/link";
    }

    @LoginRequired
    @RequestMapping("/log")
    public String log() {
        return "admin/pages/log";
    }

    @LoginRequired
    @RequestMapping("/setting")
    public String setting() {
        return "admin/pages/setting";
    }

    @LoginRequired
    @RequestMapping("/article/publish")
    public String publish() {
        return "admin/pages/publish";
    }

    @LoginRequired
    @RequestMapping(value = {"/article/edit/{id}"})
    public String edit(@PathVariable("id") Integer id) {
        if (id == null || id == 0) {
            return "admin/pages/article";
        } else {
            return "admin/pages/edit";
        }
    }


}
