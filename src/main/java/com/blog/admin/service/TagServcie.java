package com.blog.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.admin.entity.Tag;

import java.util.List;
import java.util.Map;

public interface TagServcie extends IService<Tag> {


    /**
     * 获取标签总数
     * @return
     */
    int getCount();

    /**
     * 标签管理表格数据
     * keyword 用于搜索
     */
    Map<String, Object> getTagTable(int current, int size, String keyword);

    /**
     * 获取全部标签名和id，用于添加/编辑文章时使用
     * @return
     */
    List<Map<String, Object>> getTagName();

}
