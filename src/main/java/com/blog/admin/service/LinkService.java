package com.blog.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.admin.entity.Link;

import java.util.List;
import java.util.Map;

public interface LinkService extends IService<Link> {


    /**
     * 友链管理表格数据
     * keyword 用于搜索
     * @return
     */
    Map<String, Object> getTableLink(int current, int size, String keyword);

}
