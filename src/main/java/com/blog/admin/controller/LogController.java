package com.blog.admin.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blog.admin.config.Constant;
import com.blog.admin.config.LoginRequired;
import com.blog.admin.config.RecordLog;
import com.blog.admin.dto.Result;
import com.blog.admin.entity.Log;
import com.blog.admin.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/admin")
@RestController
public class LogController {


    @Autowired
    private LogService logService;

    @GetMapping("/logs")
    @LoginRequired
    public Result getLogPage(@RequestParam(value = "currentPage") int currentPage,
                             @RequestParam(value = "size") int size,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam(value = "startTime", required = false) String startTime,
                             @RequestParam(value = "endTime", required = false) String endTime) {
        return Result.SUCCESS(logService.getLogTable(currentPage, size, keyword, startTime, endTime));
    }

    @DeleteMapping("/log/{id}")
    @LoginRequired
    public Result deleteLog(@PathVariable String id) {
        boolean b = logService.removeById(id);
        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    @DeleteMapping("/logs/{ids}")
    @LoginRequired
    public Result batchDeleteLog(@PathVariable String[] ids) {
        List<Integer> list = new ArrayList<>();
        for (String id : ids) {
            list.add(Integer.parseInt(id));
        }
        boolean b = logService.removeByIds(list);
        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }
}
