package com.blog.admin.controller;


import com.alibaba.fastjson.JSONObject;
import com.blog.admin.config.Constant;
import com.blog.admin.config.LoginRequired;
import com.blog.admin.config.RecordLog;
import com.blog.admin.dto.Result;
import com.blog.admin.entity.Classify;
import com.blog.admin.service.ClassifyService;
import com.blog.admin.utils.DateTimeUtil;
import com.blog.admin.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/admin")
@RestController
public class ClassifyController {

    @Autowired
    private ClassifyService classifyService;

    @GetMapping("/classifies")
    @LoginRequired
    public Result getClassifyPage(@RequestParam(value = "currentPage") int currentPage, @RequestParam(value = "size") int size, @RequestParam(value = "keyword", required = false) String keyword) {
        return Result.SUCCESS(classifyService.getClassifyTable(currentPage, size, keyword));
    }

    @RecordLog("新增分类")
    @PostMapping("/classify")
    @LoginRequired
    public Result addClassify(@RequestBody String data) {
        JSONObject jsonObject = JsonUtil.String2JSON(data);

        String classifyName = jsonObject.getString("classify_name");
        String classifyDec = jsonObject.getString("classify_dec");

        Classify classify = new Classify();
        classify.setClassifyName(classifyName);
        classify.setClassifyDec(classifyDec);
        classify.setClassifyCreationTime(DateTimeUtil.getCurrentDateTime());
        classify.setClassifyLastUpdateTime(DateTimeUtil.getCurrentDateTime());
        classify.setIsDelete(Constant.UN_DELETE);

        boolean save = classifyService.save(classify);
        if (save) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    /**
     * @param data
     * @return
     */
    @RecordLog("修改分类")
    @PatchMapping("/classify")
    @LoginRequired
    public Result saveClassify(@RequestBody String data) {
        JSONObject jsonObject = JsonUtil.String2JSON(data);

        String classifyName = jsonObject.getString("classify_name");
        String classifyDec = jsonObject.getString("classify_dec");
        String classifyID = jsonObject.getString("id");

        Classify classify = new Classify();
        classify.setId(Integer.parseInt(classifyID));
        classify.setClassifyName(classifyName);
        classify.setClassifyDec(classifyDec);
        classify.setClassifyLastUpdateTime(DateTimeUtil.getCurrentDateTime());

        boolean b = classifyService.updateById(classify);
        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    @RecordLog("删除分类")
    @DeleteMapping("/classify/{id}")
    @LoginRequired
    public Result deleteClassify(@PathVariable String id) {
        Classify classify = new Classify();
        classify.setId(Integer.parseInt(id));
        classify.setClassifyLastUpdateTime(DateTimeUtil.getCurrentDateTime());
        classify.setIsDelete(Constant.IS_DELETE);

        boolean b = classifyService.updateById(classify);
        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    @RecordLog("批量删除分类")
    @DeleteMapping("/classifies/{ids}")
    @LoginRequired
    public Result batchDeleteClassify(@PathVariable String[] ids) {
        List<Classify> list = new ArrayList<>();
        for (String id : ids) {
            Classify classify = new Classify();
            classify.setId(Integer.parseInt(id));
            classify.setClassifyLastUpdateTime(DateTimeUtil.getCurrentDateTime());
            classify.setIsDelete(Constant.IS_DELETE);
            list.add(classify);
        }

        boolean b = classifyService.updateBatchById(list);
        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

}
