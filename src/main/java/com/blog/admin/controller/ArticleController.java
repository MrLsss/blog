package com.blog.admin.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blog.admin.config.Constant;
import com.blog.admin.config.LoginRequired;
import com.blog.admin.config.RecordLog;
import com.blog.admin.dto.Result;
import com.blog.admin.entity.Article;
import com.blog.admin.service.ArticleService;
import com.blog.admin.service.ClassifyService;
import com.blog.admin.service.TagServcie;
import com.blog.admin.utils.DateTimeUtil;
import com.blog.admin.utils.ImgUpload;
import com.blog.admin.utils.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author liushuai
 */
@RestController
@RequestMapping("/admin")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ClassifyService classifyService;
    @Autowired
    private TagServcie tagServcie;

    @Getter
    @Setter
    private int id;

    /**
     * 从前台获取的文章属性，设置到文章对象中，用于插入或更新文章
     *
     * @param data
     * @return
     */
    private Article setArticle(String data) {
        JSONObject jsonObject = JsonUtil.String2JSON(data);

        String articleTag = jsonObject.getString("articleTag");
        String isOwn = jsonObject.getString("isOwn");
        String articleContentType = jsonObject.getString("articleContentType");
        String articleContent = jsonObject.getString("articleContent");
        String articleTitle = jsonObject.getString("articleTitle");
        String articleCoverImg = jsonObject.getString("articleCoverImg");
        String articleClassify = jsonObject.getString("articleClassify");
        String articleStatus = jsonObject.getString("articleStatus");
        String articleDec = jsonObject.getString("articleDec");
        String articleFrom = jsonObject.getString("articleFrom");
        String articleAuthor = jsonObject.getString("articleAuthor");

        //获取url
        JSONArray jsonArray = JSON.parseArray(articleCoverImg);
        String coverImg = "";
        if (jsonArray.size() > 0) {
            Object o = jsonArray.get(0);
            coverImg = JsonUtil.Object2JSON(o).getString("url");
        }

        if ("true".equals(isOwn)) {
            isOwn = Constant.IS_OWN;
        } else {
            isOwn = Constant.UN_OWN;
        }

        Article article = new Article();
        article.setArticleTitle(articleTitle);
        article.setArticleAuthor(articleAuthor);
        article.setArticleTagID(articleTag);
        article.setArticleContent(articleContent);
        article.setArticleDec(articleDec);
        article.setArticleClassifyID(articleClassify);
        article.setIsOwn(isOwn);
        article.setArticleFrom(articleFrom);
        article.setArticleCreationTime(DateTimeUtil.getCurrentDate());
        article.setArticleLastUpdateTime(DateTimeUtil.getCurrentDate());
        article.setArticleStatus(articleStatus);
        article.setArticleContentType(articleContentType);
        article.setArticleCoverImg(coverImg);
        article.setIsDelete(Constant.UN_DELETE);

        return article;
    }

    @GetMapping("/articles")
    @LoginRequired
    public Result getArticlePage(@RequestParam(value = "currentPage") int currentPage, @RequestParam(value = "size") int size, @RequestParam(value = "classify", required = false) String classify, @RequestParam(value = "keyword", required = false) String keyword) {
        return Result.SUCCESS(articleService.getTableArticle(currentPage, size, classify, keyword));
    }

    /**
     * 获取文章分类下拉框
     * <p>
     * 手动赋值了一个“全部”
     *
     * @return
     */
    @GetMapping("/articles/classifies")
    @LoginRequired
    public Result getClassifySelect() {
        return Result.SUCCESS(classifyService.getAllClassifyName());
    }

    @RecordLog("修改文章是否推荐状态")
    @PatchMapping("/article/rec")
    @LoginRequired
    public Result changeRec(@RequestBody String id) {

        JSONObject jsonObject = JSON.parseObject(id);
        int id1 = (Integer) jsonObject.get("id");

        int i = articleService.changeRec(id1);
        if (i == 1) {
            return Result.SUCCESS();
        } else if (i == 0) {
            return Result.REQUEST_PARAM_ERROR();
        } else {
            return Result.SERVICE_ERROR();
        }
    }

    @RecordLog("修改文章是否主页显示状态")
    @PatchMapping("/article/main")
    @LoginRequired
    public Result changeMain(@RequestBody String id) {

        JSONObject jsonObject = JSON.parseObject(id);
        int id1 = (Integer) jsonObject.get("id");

        int i = articleService.changeMain(id1);
        if (i == 1) {
            return Result.SUCCESS();
        } else if (i == 0) {
            return Result.REQUEST_PARAM_ERROR();
        } else {
            return Result.SERVICE_ERROR();
        }
    }

    @RecordLog("删除文章")
    @DeleteMapping("/article/{id}")
    @LoginRequired
    public Result deleteArticle(@PathVariable String id) {
        Article article = new Article();
        article.setId(Integer.parseInt(id));
        article.setArticleLastUpdateTime(DateTimeUtil.getCurrentDateTime());
        article.setIsDelete(Constant.IS_DELETE);

        boolean b = articleService.updateById(article);
        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR("删除出错！");
        }
    }

    @RecordLog("批量删除文章")
    @DeleteMapping("/articles/{ids}")
    @LoginRequired
    public Result batchDeleteArticle(@PathVariable String[] ids) {
        List<Article> list = new ArrayList<>();
        for (String id : ids) {
            Article article = new Article();
            article.setId(Integer.parseInt(id));
            article.setArticleLastUpdateTime(DateTimeUtil.getCurrentDateTime());
            article.setIsDelete(Constant.IS_DELETE);
            list.add(article);
        }
        boolean b = articleService.updateBatchById(list);
        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    /**
     * 获取所有分类，用于在添加/编辑文章处显示
     *
     * @return
     */
    @GetMapping("/publish/classifies")
    @LoginRequired
    public Result getAllClassify() {
        List<Map<String, Object>> allClassifyName = classifyService.getAllClassifyName();
        for (int i = 0; i < allClassifyName.size(); i++) {
            if ("全部".equals(allClassifyName.get(i).get("label")) && "0".equals(allClassifyName.get(i).get("value"))) {
                allClassifyName.remove(i);
                break;
            }
        }
        return Result.SUCCESS(allClassifyName);
    }

    /**
     * 获取所有标签，用于在添加/编辑文章处显示
     */
    @GetMapping("/publish/tags")
    @LoginRequired
    public Result getAllTag() {
        return Result.SUCCESS(tagServcie.getTagName());
    }


    /**
     * @param file
     * @param cover 表示上传的是封面内容
     * @return
     */
    @PostMapping("/article/{cover}")
    @LoginRequired
    public Result uploadCoverImg(@RequestBody MultipartFile file, @PathVariable("cover") String cover) {
        Map<String, String> map = ImgUpload.uploadImg(file, cover);
        return Result.SUCCESS(map);
    }

    /**
     * 上传文章内容中的图片
     *
     * @param file
     * @param content 表示上传的是文章内容中的图片
     * @param type    标识是markdown还是wangEditor
     * @return
     */
    @PostMapping("/article/{content}/{type}")
    @LoginRequired
    public void uploadContentImg(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "editormd-image-file", required = false) MultipartFile file, @PathVariable("content") String content, @PathVariable("type") String type) {
        try {
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "application/json");
            Map<String, String> map = ImgUpload.uploadImg(file, content);
            String url = map.get("url");
            if ("markdown".equals(type)) {
                response.getWriter().write("{\"success\": 1, \"message\": \"上传成功\", \"url\":  \"" + url + "\"}");
            } else {
                response.getWriter().write("{\"errno\": 0, \"data\": [\"" + url + "\"]}");
            }
        } catch (Exception e) {
            try {
                response.getWriter().write("{\"success\": 0}");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @RecordLog("发表文章")
    @PostMapping("/article")
    @LoginRequired
    public Result addArticle(@RequestBody String data) {
        Article article = setArticle(data);
        article.setIsMain(Constant.UN_MAIN);
        article.setIsRec(Constant.UN_REC);
        article.setArticleView("0");
        article.setArticleComment("0");
        if (articleService.save(article)) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    @RecordLog("保存文章草稿")
    @PostMapping("/saveArticle")
    @LoginRequired
    public Result saveArticle(@RequestBody String data) {
        Article article = setArticle(data);
        article.setIsMain(Constant.UN_MAIN);
        article.setIsRec(Constant.UN_REC);
        article.setArticleView("0");
        article.setArticleComment("0");
        if (articleService.save(article)) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    /**
     * 初始化文章编辑的数据
     *
     * @param id
     * @return
     */
    @GetMapping("/article/{id}")
    @LoginRequired
    public Result getArticleInfo(@PathVariable int id) {
        setId(id);
        return Result.SUCCESS(articleService.getArticleInfo(id));
    }

    @RecordLog("修改文章")
    @PutMapping("/article")
    @LoginRequired
    public Result editArticle(@RequestBody String data) {
        int id = getId();
        Article article = setArticle(data);
        article.setId(id);
        article.setArticleCreationTime(null);
        article.setArticleLastUpdateTime(DateTimeUtil.getCurrentDate());
        boolean b = articleService.updateById(article);
        setId(0);
        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }
}
