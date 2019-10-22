package com.blog.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.admin.config.Constant;
import com.blog.admin.dao.LogDao;
import com.blog.admin.entity.Log;
import com.blog.admin.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;


@Service("logService")
public class LogServiceImpl extends ServiceImpl<LogDao, Log> implements LogService {

    @Autowired
    private LogDao logDao;


    @Override
    public Map<String, Object> getLogTable(int current, int size, String keyword, String startTime, String endTime) {
        Map<String, Object> res = new HashMap<>();
        QueryWrapper<Log> query = new QueryWrapper<>();
        Page<Log> pageConfig = new Page<>(current, size);

        query.orderByDesc("log_time");
        if (!"".equals(keyword)) {
            query.like("log_dec", keyword).or().like("log_method", keyword);
        }
        if (!"".equals(startTime) && !"".equals(endTime)) {
            query.between("log_time", startTime, endTime);
        }

        IPage<Map<String, Object>> logPage = logDao.selectMapsPage(pageConfig, query);

        res.put("pageCode", logPage.getCurrent());
        res.put("pageSize", logPage.getSize());
        res.put("records", logPage.getRecords());
        res.put("totalPage", logPage.getTotal());

        return res;
    }



}
