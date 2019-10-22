package com.blog.admin.dao;

import com.blog.admin.common.dao.CommonMapper;
import com.blog.admin.entity.Article;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liushuai
 */
@Repository
public interface ArticleDao extends CommonMapper<Article> {


    /**
     * 获取文章归档
     * @return
     */
    List<Article> getArticleArchives();

    /**
     * 浏览量增加
     */
    void viewAdd(int id);

    /**
     * 评论增加
     */
    void commentAdd(int id);
}
