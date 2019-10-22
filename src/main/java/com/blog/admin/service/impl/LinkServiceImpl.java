package com.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.admin.config.Constant;
import com.blog.admin.dao.LinkDao;
import com.blog.admin.entity.Classify;
import com.blog.admin.entity.Link;
import com.blog.admin.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkDao, Link> implements LinkService {


    @Autowired
    private LinkDao linkDao;



    @Override
    public Map<String, Object> getTableLink(int current, int size, String keyword) {
        Map<String, Object> res = new HashMap<>();
        QueryWrapper<Link> query = new QueryWrapper<>();

        Page<Link> pageConfig = new Page<>(current, size);

        query.select("ID", "link_url", "link_name","link_dec", "link_creation_time").eq("is_delete", Constant.UN_DELETE).orderByDesc("link_creation_time");
        if (!"".equals(keyword)) {
            query.like("link_url", keyword).or().like("link_name", keyword);
        }

        IPage<Map<String, Object>> linkPage = linkDao.selectMapsPage(pageConfig, query);

        res.put("pageCode", linkPage.getCurrent());
        res.put("pageSize", linkPage.getSize());
        res.put("records", linkPage.getRecords());
        res.put("totalPage", linkPage.getTotal());

        return res;
    }
}
