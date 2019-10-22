package com.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.admin.config.Constant;
import com.blog.admin.entity.Article;
import com.blog.admin.entity.Classify;
import com.blog.admin.entity.Log;
import com.blog.admin.entity.Tag;
import com.blog.admin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


/**
 * @author liushuai
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ClassifyService classifyService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private TagServcie tagServcie;
    @Autowired
    private LogService logService;

    @Override
    public Map<String, Integer> getCount() {
        Map<String, Integer> map = new HashMap<>();
        map.put("article", articleService.getCount());
        map.put("classify", classifyService.getCount());
        map.put("comment", commentService.getCount());
        map.put("tag", tagServcie.getCount());
        return map;
    }

    /**
     * @return 分类名，该分类下的文章总数，所占百分比
     */
    @Override
    public List<Map<String, Object>> getPie() {
        List<Map<String, Object>> result = new ArrayList<>();

        QueryWrapper<Classify> classifyQuery = new QueryWrapper<>();
        classifyQuery.select("classify_name", "ID").eq("is_delete", Constant.UN_DELETE);
        List<Map<String, Object>> classifyName = classifyService.listMaps(classifyQuery);

        QueryWrapper<Article> articleQuery = new QueryWrapper<>();
        articleQuery.select("article_classify_id").eq("is_delete", Constant.UN_DELETE);
        List<Map<String, Object>> articleClassifyName = articleService.listMaps(articleQuery);

        //分类数量计数器
        int count;
        for (int i = 0; i < classifyName.size(); i++) {
            Map<String, Object> res = new HashMap<>();
            count = 0;
            for (int j = 0; j < articleClassifyName.size(); j++) {
                if ((articleClassifyName.get(j).get("article_classify_id") + "").equals(classifyName.get(i).get("ID") + "")) {
                    count++;
                }
            }
            //该分类下有文章才put进map中
            if (count != 0) {
                res.put("count", count);
                res.put("item", classifyName.get(i).get("classify_name"));
                //计算该分类占全部分类的百分比
                BigDecimal bd = new BigDecimal(((double) count) / ((double) articleClassifyName.size()));
                bd = bd.setScale(2, RoundingMode.HALF_UP);
                res.put("percent", bd);
                result.add(res);
            }

        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getTOP10() {
        List<Map<String, Object>> res = new ArrayList<>();

        QueryWrapper<Article> articleQuery = new QueryWrapper<>();
        //article_view+0将字符串转换为int类型后排序
        articleQuery.select("article_title", "article_view").eq("is_delete", Constant.UN_DELETE).orderByDesc("article_view + 0").last("limit 10");
        List<Map<String, Object>> articleView = articleService.listMaps(articleQuery);

        for (int i = articleView.size() - 1; i >= 0; i--) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", articleView.get(i).get("article_title"));
            map.put("value", articleView.get(i).get("article_view"));
            res.add(map);
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> getTagCloud() {
        List<Map<String, Object>> res = new ArrayList<>();

        QueryWrapper<Tag> tagQuery = new QueryWrapper<>();
        tagQuery.select("tag_name").eq("is_delete", Constant.UN_DELETE);
        List<Map<String, Object>> tagCloud = tagServcie.listMaps(tagQuery);

        for (int i = 0; i < tagCloud.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("x", tagCloud.get(i).get("tag_name"));
            map.put("value", new Random().nextInt(10) + 1);
            res.add(map);
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> getOperationTime() {
        List<Map<String, Object>> map = new ArrayList<>();
        QueryWrapper<Log> query = new QueryWrapper<>();
        query.select("log_user", "log_dec", "log_time").orderByDesc("log_time").last("limit 3");
        return logService.listMaps(query);
    }
}
