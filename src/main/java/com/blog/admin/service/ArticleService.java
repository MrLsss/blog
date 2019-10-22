package com.blog.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.admin.dto.ArticleArchive;
import com.blog.admin.entity.Article;

import java.util.List;
import java.util.Map;

/**
 * @author liushuai
 */
public interface ArticleService extends IService<Article> {

    //======================================后台=================================================
    /**
     * 获取文章总数
     *
     * @return
     */
    int getCount();

    /**
     * 文章管理表格数据
     * classify 用于下拉框选择
     * keyword 用于搜索
     * @return
     */
    Map<String, Object> getTableArticle(int current, int size, String classify, String keyword);

    /**
     * 修改推荐状态
     *
     * @return
     */
    int changeRec(int id);

    /**
     * 修改主页状态
     *
     * @param id
     * @return
     */
    int changeMain(int id);

    /**
     * 根据id获取文章信息
     * @param id
     * @return
     */
    Article getArticleInfo(int id);

    //======================================前台=================================================

    /**
     * 获取首页文章列表
     * @return
     */
    List<Article> getMainArticleList();

    /**
     * 获取热门文章
     * @return
     */
    List<Article> getTopArticle();

    /**
     * 获取推荐文章
     * @return
     */
    List<Article> getRecArticle();


    /**
     * 获取文章列表，分页
     * @param classify 选中的分类
     * @return
     */
    Map<String, Object> getArticleList(int current, String classify);

    /**
     * 获取文章详细信息
     * @param id
     * @return
     */
    Article getArticle(int id);


    /**
     * 获取文章归档数据
     * @return
     */
    List<ArticleArchive> getArticleArchives();

    /**
     * 增加文章的浏览数量
     */
    void addArticleView(int id);

    /**
     * 增加文章的评论数量
     */
    void addArticleComment(int id);

    /**
     * 标签下的文章列表 分页
     * @return
     */
    Map<String, Object> getTagArticle(int current, int tagId);
}
