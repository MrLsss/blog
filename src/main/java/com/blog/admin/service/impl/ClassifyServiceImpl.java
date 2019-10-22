package com.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.admin.config.Constant;
import com.blog.admin.dao.ClassifyDao;
import com.blog.admin.entity.Article;
import com.blog.admin.entity.Classify;
import com.blog.admin.entity.Tag;
import com.blog.admin.service.ClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("classifyService")
public class ClassifyServiceImpl extends ServiceImpl<ClassifyDao, Classify> implements ClassifyService {

    @Autowired
    private ClassifyDao classifyDao;

    @Override
    public int getCount() {
        QueryWrapper<Classify> query = new QueryWrapper<>();
        query.eq("is_delete", Constant.UN_DELETE);
        return classifyDao.selectCount(query);
    }

    @Override
    public List<Map<String, Object>> getAllClassifyName() {
        List<Map<String, Object>> res = new ArrayList<>();
        QueryWrapper<Classify> query = new QueryWrapper<>();

        query.select("classify_name", "id").eq("is_delete", Constant.UN_DELETE);
        List<Map<String, Object>> allClassifyName = classifyDao.selectMaps(query);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("value", "0");
        map1.put("label", "全部");
        res.add(map1);

        for (Map<String, Object> item : allClassifyName) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", item.get("id"));
            map.put("label", item.get("classify_name"));
            res.add(map);
        }
        return res;
    }

    @Override
    public Map<String, Object> getClassifyTable(int current, int size, String keyword) {
        Map<String, Object> res = new HashMap<>();
        QueryWrapper<Classify> query = new QueryWrapper<>();

        Page<Classify> pageConfig = new Page<>(current, size);

        query.select("ID", "classify_name", "classify_dec","classify_creation_time", "classify_last_update_time").eq("is_delete", Constant.UN_DELETE).orderByDesc("classify_creation_time");
        if (!"".equals(keyword)) {
            query.like("classify_name", keyword);
        }

        IPage<Map<String, Object>> classifyPage = classifyDao.selectMapsPage(pageConfig, query);

        res.put("pageCode", classifyPage.getCurrent());
        res.put("pageSize", classifyPage.getSize());
        res.put("records", classifyPage.getRecords());
        res.put("totalPage", classifyPage.getTotal());

        return res;
    }

    @Override
    public List<Classify> getAllClassify() {
        List<Classify> classifies = new ArrayList<>();
        Classify classify = new Classify();
        classify.setId(0);
        classify.setClassifyName("全部");
        classify.setClassifyDec("全部文章");
        classifies.add(classify);
        QueryWrapper<Classify> query = new QueryWrapper<>();
        query.select("ID", "classify_name", "classify_dec").eq("is_delete", Constant.UN_DELETE);
        classifies.addAll(classifyDao.selectList(query));
        return classifies;
    }
}
