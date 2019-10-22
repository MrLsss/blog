package com.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.admin.config.Constant;
import com.blog.admin.dao.TagDao;
import com.blog.admin.entity.Tag;
import com.blog.admin.service.TagServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author liushuai
 */
@Service
public class TagServcieImpl extends ServiceImpl<TagDao, Tag> implements TagServcie {

    @Autowired
    private TagDao tagDao;

    @Override
    public int getCount() {
        QueryWrapper<Tag> query = new QueryWrapper<>();
        query.eq("is_delete", Constant.UN_DELETE);
        return tagDao.selectCount(query);
    }

    @Override
    public Map<String, Object> getTagTable(int current, int size, String keyword) {
        Map<String, Object> res = new HashMap<>();
        QueryWrapper<Tag> query = new QueryWrapper<>();
        Page<Tag> pageConfig = new Page<>(current, size);

        query.eq("is_delete", Constant.UN_DELETE).orderByDesc("tag_creation_time");
        if (!"".equals(keyword)) {
            query.like("tag_name", keyword);
        }

        IPage<Map<String, Object>> tagPage = tagDao.selectMapsPage(pageConfig, query);

        res.put("pageCode", tagPage.getCurrent());
        res.put("pageSize", tagPage.getSize());
        res.put("records", tagPage.getRecords());
        res.put("totalPage", tagPage.getTotal());

        return res;
    }

    @Override
    public List<Map<String, Object>> getTagName() {
        QueryWrapper<Tag> query = new QueryWrapper<>();
        query.select("tag_name", "ID");
        List<Map<String, Object>> res = tagDao.selectMaps(query);
        return res;
    }
}
