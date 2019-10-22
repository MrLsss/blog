package com.blog.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.admin.entity.Classify;

import java.util.List;
import java.util.Map;

public interface ClassifyService extends IService<Classify> {

    /**
     * 获取分类总数
     * @return
     */
    int getCount();

    /**
     * 获取所有分类名
     * @return
     */
    List<Map<String, Object>> getAllClassifyName();

    /**
     * 分类管理表格数据
     * keyword 用于搜索
     * @return
     */
    Map<String, Object> getClassifyTable(int current, int size, String keyword);

    /**
     * 获取所有分类，用于前台显示
     * @return
     */
    List<Classify> getAllClassify();
}
