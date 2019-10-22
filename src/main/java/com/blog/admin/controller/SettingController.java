package com.blog.admin.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.blog.admin.config.LoginRequired;
import com.blog.admin.config.RecordLog;
import com.blog.admin.dto.Result;
import com.blog.admin.entity.SiteInfo;
import com.blog.admin.entity.WebMasterInfo;
import com.blog.admin.service.SiteInfoService;
import com.blog.admin.service.WebMasterInfoService;
import com.blog.admin.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/setting")
@RestController
public class SettingController {


    @Autowired
    private SiteInfoService siteInfoService;
    @Autowired
    private WebMasterInfoService infoServcie;


    @GetMapping("/getSiteBaseInfo")
    @LoginRequired
    public Result getSiteBaseInfo() {
        SiteInfo one = siteInfoService.getOne(new QueryWrapper<>());
        return Result.SUCCESS(one);
    }

    @GetMapping("/getMasterInfo")
    @LoginRequired
    public Result getMasterInfo() {
        WebMasterInfo one = infoServcie.getOne(new QueryWrapper<>());
        return Result.SUCCESS(one);
    }

    @RecordLog("修改登录密码")
    @PostMapping("/modifyPwd")
    @LoginRequired
    public Result modifyPwd(@RequestBody String data) {

        JSONObject jsonObject = JsonUtil.String2JSON(data);

        String oldPass = jsonObject.getString("oldPass");
        String checkPass = jsonObject.getString("checkPass");

        WebMasterInfo webMasterInfo = new WebMasterInfo();

        WebMasterInfo masterInfo = infoServcie.getOne(new QueryWrapper<WebMasterInfo>().select("wm_password"));
        if (oldPass.equals(masterInfo.getWmPassword())) {
            webMasterInfo.setWmPassword(checkPass);
            boolean update = infoServcie.update(webMasterInfo, new UpdateWrapper<>());
            if (update) {
                return Result.SUCCESS();
            } else {
                return Result.ERROR();
            }
        } else {
            return new Result(500, "原密码错误");
        }
    }

}
