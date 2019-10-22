package com.blog.admin.controller;


import com.alibaba.fastjson.JSONObject;
import com.blog.admin.config.Constant;
import com.blog.admin.config.LoginRequired;
import com.blog.admin.config.RecordLog;
import com.blog.admin.dto.Result;
import com.blog.admin.entity.Tag;
import com.blog.admin.service.TagServcie;
import com.blog.admin.utils.DateTimeUtil;
import com.blog.admin.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/admin")
@RestController
public class TagController {


    @Autowired
    private TagServcie tagServcie;

    @GetMapping("/tags")
    @LoginRequired
    public Result getTagPage(@RequestParam(value = "currentPage") int currentPage, @RequestParam(value = "size") int size, @RequestParam(value = "keyword", required = false) String keyword) {
        return Result.SUCCESS(tagServcie.getTagTable(currentPage, size, keyword));
    }

    @RecordLog("添加标签")
    @PostMapping("/tag")
    @LoginRequired
    public Result addTag(@RequestBody String data) {
        JSONObject jsonObject = JsonUtil.String2JSON(data);

        String tagName = jsonObject.getString("tag_name");

        Tag tag = new Tag();
        tag.setTagName(tagName);
        tag.setTagCreationTime(DateTimeUtil.getCurrentDateTime());
        tag.setTagLastUpdateTime(DateTimeUtil.getCurrentDateTime());
        tag.setIsDelete(Constant.UN_DELETE);

        boolean save = tagServcie.save(tag);
        if (save) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    @RecordLog("修改标签")
    @PatchMapping("/tag")
    @LoginRequired
    public Result saveTag(@RequestBody String data) {
        JSONObject jsonObject = JsonUtil.String2JSON(data);

        String tagName = jsonObject.getString("tag_name");
        String tagID = jsonObject.getString("id");

        Tag tag = new Tag();
        tag.setId(Integer.parseInt(tagID));
        tag.setTagName(tagName);
        tag.setTagLastUpdateTime(DateTimeUtil.getCurrentDateTime());

        boolean b = tagServcie.updateById(tag);

        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    @RecordLog("删除标签")
    @DeleteMapping("/tag/{id}")
    @LoginRequired
    public Result deleteTag(@PathVariable String id) {
        Tag tag = new Tag();
        tag.setId(Integer.parseInt(id));
        tag.setTagLastUpdateTime(DateTimeUtil.getCurrentDateTime());
        tag.setIsDelete(Constant.IS_DELETE);

        boolean b = tagServcie.updateById(tag);
        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    @RecordLog("批量删除标签")
    @DeleteMapping("/tags/{ids}")
    @LoginRequired
    public Result batchDeleteTag(@PathVariable String[] ids) {
        List<Tag> list = new ArrayList<>();
        for (String id : ids) {
            Tag tag = new Tag();
            tag.setId(Integer.parseInt(id));
            tag.setTagLastUpdateTime(DateTimeUtil.getCurrentDateTime());
            tag.setIsDelete(Constant.IS_DELETE);

            list.add(tag);
        }
        boolean b = tagServcie.updateBatchById(list);
        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }


}
