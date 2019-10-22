package com.blog.admin.controller;


import com.blog.admin.config.LoginRequired;
import com.blog.admin.dto.Result;
import com.blog.admin.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author liushuai
 * 后台首页controller
 */
@RestController
@RequestMapping("/admin/index")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 获取首页的四个数量统计
     * @return
     */
    @GetMapping("/counts")
    @LoginRequired
    public Result getArticleCount() {
        return Result.SUCCESS(adminService.getCount());
    }

    /**
     * 获取饼状图数据
     */
    @GetMapping("/pie")
    @LoginRequired
    public Result getPie() {
        return Result.SUCCESS(adminService.getPie());
    }

    /**
     * 获取文章访问top10的数据
     * @return
     */
    @GetMapping("/top10")
    @LoginRequired
    public Result getTOP10() {
        return Result.SUCCESS(adminService.getTOP10());
    }

    /**
     * 获取标签云的数据
     */
    @GetMapping("/tagCloud")
    @LoginRequired
    public Result getTagCloud() {
        return Result.SUCCESS(adminService.getTagCloud());
    }

    /**
     * 获取操作时间线
     * @return
     */
    @GetMapping("/operationTime")
    @LoginRequired
    public Result getOperationTime() {
        return Result.SUCCESS(adminService.getOperationTime());
    }

}
