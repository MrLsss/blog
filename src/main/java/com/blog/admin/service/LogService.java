package com.blog.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.admin.entity.Log;

import java.util.Map;

public interface LogService extends IService<Log> {


    /**
     * 日志管理表格数据
     * @param current
     * @param size
     * @param keyword 搜搜关键字
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return
     */
    Map<String, Object> getLogTable(int current, int size, String keyword, String startTime, String endTime);

}
