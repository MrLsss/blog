package com.blog.admin.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blog.admin.config.Constant;
import com.blog.admin.config.LoginRequired;
import com.blog.admin.config.RecordLog;
import com.blog.admin.dto.Result;
import com.blog.admin.entity.Link;
import com.blog.admin.service.LinkService;
import com.blog.admin.utils.DateTimeUtil;
import com.blog.admin.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class LinkController {


    @Autowired
    private LinkService linkService;


    @GetMapping("/links")
    @LoginRequired
    public Result getLinkPage(@RequestParam(value = "currentPage") int currentPage,
                              @RequestParam(value = "size") int size,
                              @RequestParam(value = "keyword", required = false) String keyword) {
        return Result.SUCCESS(linkService.getTableLink(currentPage, size, keyword));
    }

    @RecordLog("删除友链")
    @DeleteMapping("/link/{id}")
    @LoginRequired
    public Result deleteLink(@PathVariable String id) {
        Link link = new Link();
        link.setId(Integer.parseInt(id));
        link.setIsDelete(Constant.IS_DELETE);

        boolean b = linkService.updateById(link);
        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    @RecordLog("批量删除友链")
    @DeleteMapping("/links/{ids}")
    @LoginRequired
    public Result batchDeleteLink(@PathVariable String[] ids) {
        List<Link> list = new ArrayList<>();
        for (String id : ids) {
            Link link = new Link();
            link.setId(Integer.parseInt(id));
            link.setIsDelete(Constant.IS_DELETE);

            list.add(link);
        }
        boolean b = linkService.updateBatchById(list);
        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    @RecordLog("添加友链")
    @PostMapping("/link")
    @LoginRequired
    public Result addLink(@RequestBody String data) {
        JSONObject jsonObject = JsonUtil.String2JSON(data);
        String linkName = jsonObject.getString("link_name");
        String linkUrl = jsonObject.getString("link_url");
        String linkDec = jsonObject.getString("link_dec");

        Link link = new Link();
        link.setLinkUrl(linkUrl);
        link.setLinkName(linkName);
        link.setLinkDec(linkDec);
        link.setLinkCreationTime(DateTimeUtil.getCurrentDateTime());
        link.setIsDelete(Constant.UN_DELETE);

        boolean save = linkService.save(link);
        if (save) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    @RecordLog("修改友链")
    @PutMapping("/link")
    @LoginRequired
    public Result saveLink(@RequestBody String data) {
        JSONObject jsonObject = JsonUtil.String2JSON(data);

        String linkId = jsonObject.getString("id");
        String linkName = jsonObject.getString("link_name");
        String linkUrl = jsonObject.getString("link_url");
        String linkDec = jsonObject.getString("link_dec");

        Link link = new Link();
        link.setId(Integer.valueOf(linkId));
        link.setLinkUrl(linkUrl);
        link.setLinkName(linkName);
        link.setLinkDec(linkDec);
        link.setLinkCreationTime(DateTimeUtil.getCurrentDateTime());

        boolean b = linkService.updateById(link);
        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }


}
