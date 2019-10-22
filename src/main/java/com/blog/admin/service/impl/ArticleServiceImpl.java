package com.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.admin.config.Constant;
import com.blog.admin.dao.ArticleDao;
import com.blog.admin.dao.ClassifyDao;
import com.blog.admin.dao.TagDao;
import com.blog.admin.dto.ArticleArchive;
import com.blog.admin.entity.Article;
import com.blog.admin.entity.Classify;
import com.blog.admin.entity.Tag;
import com.blog.admin.service.ArticleService;
import com.blog.admin.service.ClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @author liushuai
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ClassifyDao classifyDao;
    @Autowired
    private TagDao tagDao;

    @Override
    public int getCount() {
        QueryWrapper<Article> query = new QueryWrapper<>();
        query.eq("is_delete", Constant.UN_DELETE);
        return articleDao.selectCount(query);
    }

    @Override
    public Map<String, Object> getTableArticle(int current, int size, String classify, String keyword) {
        Map<String, Object> res = new HashMap<>();
        QueryWrapper<Article> query = new QueryWrapper<>();

        Page<Article> pageConfig = new Page<>(current, size);
        query.select("ID", "article_title", "article_classify_id", "is_main", "is_rec", "article_view", "article_comment", "article_status", "article_creation_time", "article_last_update_time", "is_own").eq("is_delete", Constant.UN_DELETE).orderByDesc("article_creation_time");
        if (!"0".equals(classify)) {
            query.eq("article_classify_id", classify);
        }
        if (!"".equals(keyword)) {
            query.like("article_title", keyword);
        }

        IPage<Article> articleIPage = articleDao.selectPage(pageConfig, query);

        //查找文章对应的分类名称
        List<Article> records = articleIPage.getRecords();
        for (Article record : records) {
            String articleClassifyID = record.getArticleClassifyID();
            Classify byId = classifyDao.selectById(articleClassifyID);
            record.setArticleClassify(byId.getClassifyName());
        }

        res.put("pageCode", articleIPage.getCurrent());
        res.put("pageSize", articleIPage.getSize());
        res.put("records", records);
        res.put("totalPage", articleIPage.getTotal());

        return res;
    }

    @Override
    public int changeRec(int id) {
        QueryWrapper<Article> query = new QueryWrapper<>();
        query.select("is_rec").eq("is_delete", Constant.UN_DELETE).eq("id", id);
        Article articleIsRec = articleDao.selectOne(query);
        if (articleIsRec.getIsRec() == null || "".equals(articleIsRec.getIsRec())) {
            return 0;
        }

        Article article = new Article();
        article.setId(id);

        if (Constant.IS_REC.equals(articleIsRec.getIsRec())) {
            article.setIsRec(Constant.UN_REC);
        } else {
            article.setIsRec(Constant.IS_REC);
        }
        return articleDao.updateById(article);
    }

    @Override
    public int changeMain(int id) {
        QueryWrapper<Article> query = new QueryWrapper<>();
        query.select("is_main").eq("is_delete", 0).eq("id", id);
        Article articleIsMain = articleDao.selectOne(query);
        if (articleIsMain.getIsMain() == null || "".equals(articleIsMain.getIsMain())) {
            return 0;
        }
        Article article = new Article();
        article.setId(id);

        if (Constant.IS_MAIN.equals(articleIsMain.getIsMain())) {
            article.setIsMain(Constant.UN_MAIN);
        } else {
            article.setIsMain(Constant.IS_MAIN);
        }
        return articleDao.updateById(article);
    }

    @Override
    public Article getArticleInfo(int id) {
        QueryWrapper<Article> query = new QueryWrapper<>();
        query.eq("is_delete", Constant.UN_DELETE).eq("ID", id);
        query.select(Article.class, info -> !"is_delete".equals(info.getColumn()) && !"is_main".equals(info.getColumn()) && !"is_rec".equals(info.getColumn()) && !"article_view".equals(info.getColumn()) && !"article_comment".equals(info.getColumn()) && !"article_creation_time".equals(info.getColumn()) && !"article_last_update_time".equals(info.getColumn()) && !"article_tag".equals(info.getColumn()) && !"article_classify".equals(info.getColumn()));

        Article article = articleDao.selectOne(query);
        System.out.println(article);

        String articleClassifyID = article.getArticleClassifyID();
        Classify classify = classifyDao.selectOne(new QueryWrapper<Classify>().select("classify_name").eq("ID", articleClassifyID));
        String classifyName = classify.getClassifyName();

        String articleTagID = article.getArticleTagID();
        String[] split = articleTagID.split(",");
        List<Map<String, Object>> tagList = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            Tag tag = tagDao.selectOne(new QueryWrapper<Tag>().select("tag_name").eq("ID", split[i]));
            Map<String, Object> tagMap = new HashMap<>();
            tagMap.put("id", split[i]);
            tagMap.put("text", tag.getTagName());
            tagList.add(tagMap);
        }

        article.setArticleClassify(classifyName);
        article.setArticleTag(tagList);

        return article;
    }

    @Override
    public List<Article> getMainArticleList() {
        QueryWrapper<Article> query = new QueryWrapper<>();
        query.select("ID", "article_title", "article_classify_id", "article_cover_img").eq("is_delete", Constant.UN_DELETE).eq("is_main", Constant.IS_MAIN);
        List<Article> articles = articleDao.selectList(query);
        for (Article article : articles) {
            String articleClassifyID = article.getArticleClassifyID();
            Classify classify = classifyDao.selectOne(new QueryWrapper<Classify>().eq("ID", articleClassifyID).eq("is_delete", Constant.UN_DELETE));
            article.setArticleClassify(classify.getClassifyName());
        }
        return articles;
    }

    @Override
    public List<Article> getTopArticle() {
        QueryWrapper<Article> query = new QueryWrapper<>();
        query.select("ID", "article_title").eq("is_delete", Constant.UN_DELETE).orderByDesc("article_view + 0").last("limit 5");
        return articleDao.selectList(query);
    }

    @Override
    public List<Article> getRecArticle() {
        QueryWrapper<Article> query = new QueryWrapper<>();
        query.select("ID", "article_title").eq("is_delete", Constant.UN_DELETE).eq("is_rec", Constant.IS_REC);
        return articleDao.selectList(query);
    }

    @Override
    public Map<String, Object> getArticleList(int current, String classify) {
        QueryWrapper<Article> query = new QueryWrapper<>();
        query.select("ID", "article_title", "article_classify_id", "article_author", "article_creation_time", "article_view", "article_comment", "article_dec", "article_cover_img", "is_own").eq("is_delete", Constant.UN_DELETE).orderByDesc("article_creation_time");
        Page<Article> pageConfig = new Page<>(current, 5);

        if (!"0".equals(classify)) {
            query.eq("article_classify_id", classify);
        }

        IPage<Article> articleIPage = articleDao.selectPage(pageConfig, query);

        List<Article> records = articleIPage.getRecords();
        for (Article record : records) {
            String articleClassifyID = record.getArticleClassifyID();
            Classify classify1 = classifyDao.selectOne(new QueryWrapper<Classify>().select("classify_name").eq("ID", articleClassifyID));
            record.setArticleClassify(classify1.getClassifyName());
        }
        Map<String, Object> map = new HashMap<>();

        //总记录数
//        long total = articleIPage.getTotal();
        //总页数
        long pages = articleIPage.getPages();
        int[] pageIndex = new int[(int) pages];
        for (int i = 0; i < pages; i++) {
            pageIndex[i] = i + 1;
        }
        long currentPage = articleIPage.getCurrent();
        map.put("current", currentPage);
        map.put("pageIndex", pageIndex);
        map.put("records", records);
        map.put("pages", pages);
        return map;
    }

    @Override
    public Article getArticle(int id) {
        QueryWrapper<Article> query = new QueryWrapper<>();
        query.select("ID", "article_title", "article_author", "article_content", "article_tag_id", "article_dec", "is_own", "article_view", "article_comment", "article_creation_time", "article_content_type", "article_cover_img").eq("is_delete", Constant.UN_DELETE).eq("ID", id);
        Article article = articleDao.selectOne(query);

        String articleTagIDs = article.getArticleTagID();
        String[] split = articleTagIDs.split(",");
        List<Map<String, Object>> tagList = new ArrayList<>();

        for (String s : split) {
            Map<String, Object> map = new HashMap<>();
            Tag tag = tagDao.selectOne(new QueryWrapper<Tag>().select("tag_name", "ID").eq("ID", s).eq("is_delete", Constant.UN_DELETE));
            map.put(String.valueOf(tag.getId()), tag.getTagName());
            tagList.add(map);
        }
        article.setArticleTag(tagList);
        return article;
    }

    @Override
    public List<ArticleArchive> getArticleArchives() {
        List<Article> articleArchives = articleDao.getArticleArchives();

        Set<String> monthSet = new HashSet<>();
        //返回值
        List<ArticleArchive> archiveList = new ArrayList<>();

        for (int i = articleArchives.size() - 1; i >= 0; i--) {
            String articleCreationTime = articleArchives.get(i).getArticleCreationTime();
            String substring = articleCreationTime.substring(0, articleCreationTime.lastIndexOf("-"));

            if (articleArchives.get(i).getArticleCreationTime().startsWith(substring)) {
                if (monthSet.add(substring)) {
                    archiveList.add(new ArticleArchive(substring, null));
                }
            }
        }


        for (int i = 0; i < archiveList.size(); i++) {
            List<Article> articles = new ArrayList<>();
            for (int j = 0; j < articleArchives.size(); j++) {
                if (articleArchives.get(j).getArticleCreationTime().startsWith(archiveList.get(i).getDate())) {
                    articles.add(articleArchives.get(j));
                }
            }
            archiveList.get(i).setArticleList(articles);
        }

        return archiveList;
    }

    @Override
    public void addArticleView(int id) {
        articleDao.viewAdd(id);
    }

    @Override
    public void addArticleComment(int id) {
        articleDao.commentAdd(id);
    }

    @Override
    public Map<String, Object> getTagArticle(int current, int tagId) {
        Map<String, Object> res = new HashMap<>();

        QueryWrapper<Article> query = new QueryWrapper<>();
        query.select("ID", "article_title", "article_classify_id", "article_author", "article_creation_time",
                "article_view", "article_comment", "article_dec", "article_cover_img", "is_own")
                .eq("is_delete", Constant.UN_DELETE).like("article_tag_id", tagId)
                .orderByDesc("article_creation_time");
        Page<Article> pageConfig = new Page<>(current, 5);

        IPage<Article> articleIPage = articleDao.selectPage(pageConfig, query);

        List<Article> records = articleIPage.getRecords();
        for (Article record : records) {
            String articleClassifyID = record.getArticleClassifyID();
            Classify classify1 = classifyDao.selectOne(new QueryWrapper<Classify>().select("classify_name").eq("ID", articleClassifyID));
            record.setArticleClassify(classify1.getClassifyName());
        }

        long pages = articleIPage.getPages();

        int[] pageIndex = new int[(int) pages];

        for (int i = 0; i < pages; i++) {
            pageIndex[i] = i + 1;
        }
        long currentPage = articleIPage.getCurrent();
        res.put("current", currentPage);
        res.put("pageIndex", pageIndex);
        res.put("pages", pages);
        res.put("records", records);
        return res;
    }
}
