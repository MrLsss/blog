package com.blog.site.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.admin.config.Constant;
import com.blog.admin.dto.ArticleArchive;
import com.blog.admin.dto.CommentDTO;
import com.blog.admin.dto.Result;
import com.blog.admin.entity.*;
import com.blog.admin.service.*;
import com.blog.admin.utils.AccountUtil;
import com.blog.admin.utils.DateTimeUtil;
import com.blog.admin.utils.IPUtils;
import com.blog.admin.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class SiteController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ClassifyService classifyService;
    @Autowired
    private SiteInfoService infoService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private WebMasterInfoService masterInfoService;
    @Autowired
    private TagServcie tagServcie;

    @RequestMapping(value = {"", "/"})
    public String index() {
        return "site/index";
    }

    @RequestMapping("/list")
    public String list() {
        return "site/pages/list";
    }

    @RequestMapping("/msgboard")
    public String msgboard() {
        return "site/pages/msgboard";
    }

    @RequestMapping("/link")
    public String link() {
        return "site/pages/link";
    }

    @RequestMapping("/archives")
    public String archives() {
        return "site/pages/archives";
    }

    @RequestMapping("/about")
    public String about() {
        return "site/pages/about";
    }

    @RequestMapping("/tag/{id}")
    public String tag(@PathVariable(value = "id") int id) {
        return "site/pages/tag";
    }

    /**
     * 标签下的文章列表
     * @param id
     * @return
     */
    @GetMapping("/getTagArticle")
    public String getTagArticle(Model model,
                                @RequestParam(value = "id") int id,
                                @RequestParam(value = "current", required = false) Integer current) {
        int current1 = current == null ? 1 : current;
        Map<String, Object> tagArticle = articleService.getTagArticle(current1, id);

        List<Article> articles = (List<Article>) tagArticle.get("records");
        int[] pageIndex = (int[]) tagArticle.get("pageIndex");
        Long currentPage = (Long) tagArticle.get("current");
        Long pages = (Long) tagArticle.get("pages");
        model.addAttribute("articleList", articles);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("current", currentPage);
        model.addAttribute("pages", pages);
        return "site/pages/tag::articleList";
    }

    @GetMapping("/getTagName")
    @ResponseBody
    public Result getTagName(@RequestParam(value = "id") int id) {
        Tag one = tagServcie.getOne(new QueryWrapper<Tag>().eq("is_delete", Constant.UN_DELETE).eq("ID", id));
        return Result.SUCCESS(one);
    }

    /**
     * 获得文章详细信息
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/article/{id}")
    public String article(@PathVariable(value = "id") int id, Model model) {
        Article article = articleService.getArticle(id);
        model.addAttribute("article", article);
        return "site/pages/blogdetail";
    }

    @PostMapping("/addArticleView")
    @ResponseBody
    public Result addArticleView(@RequestParam(value = "id") int id,
                                 HttpServletRequest request) {
        String ip = IPUtils.getIP(request);
        if (!"127.0.0.1".equals(ip)) {
            articleService.addArticleView(id);
        }
        return Result.SUCCESS();
    }

    /**
     * 获取文章评论
     * @param id
     * @return
     */
    @GetMapping("/getArticleComment")
    @ResponseBody
    public Result getArticleComment(@RequestParam(value = "id") int id) {
        List<CommentDTO> articleComment = commentService.getArticleComment(id);
        return Result.SUCCESS(articleComment);
    }

    /**
     * 获取推荐和热门文章
     * @param model
     * @return
     */
    @GetMapping("/getRecAndTopArticle")
    public String getRecAndTopArticle(Model model) {
        List<Article> recArticle = articleService.getRecArticle();
        List<Article> topArticle = articleService.getTopArticle();
        model.addAttribute("topArticle", topArticle);
        model.addAttribute("recArticle", recArticle);
        return "site/common/header::topAndRec";
    }

    /**
     * 获取网站logo
     * @param
     * @return
     */
    @GetMapping("/getSiteLogo")
    @ResponseBody
    public Result getSiteLogo() {
        SiteInfo siteinfo = infoService.getOne(new QueryWrapper<SiteInfo>().select("site_img"));
        return Result.SUCCESS(siteinfo.getSiteImg());
    }

    /**
     * 获取站长信息
     * @return
     */
    @GetMapping("/getMasterInfo")
    @ResponseBody
    public Result getMasterInfo() {
        WebMasterInfo one = masterInfoService.getOne(new QueryWrapper<>());
        return Result.SUCCESS(one);
    }

    /**
     * 获取主页文章列表
     * @param
     * @return
     */
    @GetMapping("/getMainArticleList")
    @ResponseBody
    public Result getMainArticleList() {
        List<Article> mainArticleList = articleService.getMainArticleList();
        return Result.SUCCESS(mainArticleList);
    }

    /**
     * 获取分类列表
     */
    @GetMapping("/getClassifyList")
    public String getClassifyList(Model model) {
        List<Classify> allClassify = classifyService.getAllClassify();
        model.addAttribute("classifyList", allClassify);
        return "site/pages/list::classifyList";
    }

    /**
     * 获取文章列表，显示所有文章
     */
    @GetMapping("/getArticleList")
    public String getArticleList(Model model, @RequestParam(value = "currentPage") int currentPage,
                                 @RequestParam(value = "classify") String classify) {
        Map<String, Object> articlePage = articleService.getArticleList(currentPage, classify);
        List<Article> articleList = (List<Article>) articlePage.get("records");
        int[] pageIndex = (int[]) articlePage.get("pageIndex");
        Long current = (Long) articlePage.get("current");
        Long pages = (Long) articlePage.get("pages");
        model.addAttribute("articleList", articleList);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("current", current);
        model.addAttribute("pages", pages);
        return "site/pages/list::articleList";
    }

    /**
     * 获取归档数据
     * @return
     */
    @GetMapping("/getArchives")
    public String getArchives(Model model) {
        List<ArticleArchive> articleArchives = articleService.getArticleArchives();
        model.addAttribute("archivesList", articleArchives);
        return "site/pages/archives::archivesList";
    }

    /**
     * 获取友链数据
     * @param model
     * @return
     */
    @GetMapping("/getLinks")
    public String getLinks(Model model) {
        List<Link> list = linkService.list(new QueryWrapper<Link>().eq("is_delete", Constant.UN_DELETE));
        model.addAttribute("linkList", list);
        return "site/pages/link::linkList";
    }

    /**
     * 新增文章回复
     * @return
     */
    @PostMapping("/addArticleReply")
    @ResponseBody
    public Result addArticleReply(@RequestParam(value = "pid") String pid,
                           @RequestParam(value = "nickname") String nickname,
                           @RequestParam(value = "email") String email,
                           @RequestParam(value = "content") String content,
                           @RequestParam(value = "comment_from") String commentFrom,
                           @RequestParam(value = "comment_for") String commentFor,
                           @RequestParam(value = "address") String address,
                           HttpServletRequest request) {
        Comment comment = new Comment();
        comment.setId(AccountUtil.getUUID());
        comment.setCommentNickname(nickname);
        comment.setCommentContent(content);
        comment.setCommentIp(IPUtils.getIP(request));
        comment.setCommentAddress(address);
        comment.setCommentEmail(email);
        comment.setCommentTime(DateTimeUtil.getCurrentDateTime());
        comment.setCommentFrom(commentFrom);
        comment.setCommentFor(commentFor);
        comment.setCommentFather(pid);
        comment.setCommentStatus(Constant.AUDIT_WAIT);
        comment.setCommentRemark("");
        comment.setIsDelete(Constant.UN_DELETE);

        boolean save = commentService.save(comment);
        if (save) {
            articleService.addArticleComment(Integer.parseInt(commentFor));
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    /**
     * 新增文章评论
     */
    @PostMapping("/addArticleComment")
    @ResponseBody
    public Result addArticleComment(@RequestParam(value = "nickname") String nickname,
                             @RequestParam(value = "email") String email,
                             @RequestParam(value = "content") String content,
                             @RequestParam(value = "comment_from") String commentFrom,
                             @RequestParam(value = "comment_for") String commentFor,
                             @RequestParam(value = "address") String address,
                             HttpServletRequest request) {
        Comment comment = new Comment();
        comment.setId(AccountUtil.getUUID());
        comment.setCommentNickname(nickname);
        comment.setCommentContent(content);
        comment.setCommentIp(IPUtils.getIP(request));
        comment.setCommentAddress(address);
        comment.setCommentEmail(email);
        comment.setCommentTime(DateTimeUtil.getCurrentDateTime());
        comment.setCommentFrom(commentFrom);
        comment.setCommentFor(commentFor);
        comment.setCommentFather("");
        comment.setCommentStatus(Constant.AUDIT_WAIT);
        comment.setCommentRemark("");
        comment.setIsDelete(Constant.UN_DELETE);

        boolean save = commentService.save(comment);
        if (save) {
            String ip = IPUtils.getIP(request);
            if (!"127.0.0.1".equals(ip)) {
                articleService.addArticleComment(Integer.parseInt(commentFor));
            }
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    /**
     * 获取留言板数据
     * @return
     */
    @GetMapping("/getMsgboardComment")
    @ResponseBody
    public Result getMsgboardComment() {
        List<CommentDTO> msgboardComment = commentService.getMsgboardComment();
        return Result.SUCCESS(msgboardComment);
    }

    /**
     * 新增留言板留言
     */
    @PostMapping("/addMsgboardComment")
    @ResponseBody
    public Result addMsgboardComment(@RequestBody String data,
                                     HttpServletRequest request) {
        JSONObject jsonObject = JsonUtil.String2JSON(data);
        String nickname = jsonObject.getString("nickname");
        String email = jsonObject.getString("email");
        String content = jsonObject.getString("content");
        String commentFrom = jsonObject.getString("comment_from");
        String address = jsonObject.getString("address");

        Comment comment = new Comment();
        comment.setId(AccountUtil.getUUID());
        comment.setCommentNickname(nickname);
        comment.setCommentContent(content);
        comment.setCommentIp(IPUtils.getIP(request));
        comment.setCommentAddress(address);
        comment.setCommentEmail(email);
        comment.setCommentTime(DateTimeUtil.getCurrentDateTime());
        comment.setCommentFrom(commentFrom);
        comment.setCommentFor("");
        comment.setCommentFather("");
        comment.setCommentStatus(Constant.AUDIT_WAIT);
        comment.setCommentRemark("");
        comment.setIsDelete(Constant.UN_DELETE);

        boolean save = commentService.save(comment);
        if (save) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    /**
     * 留言板回复
     * @param data
     * @param request
     * @return
     */
    @PostMapping("/addMsgboardReply")
    @ResponseBody
    public Result addMsgboardReply(@RequestBody String data,
                                     HttpServletRequest request) {
        JSONObject jsonObject = JsonUtil.String2JSON(data);

        String address = jsonObject.getString("address");
        String nickname = jsonObject.getString("nickname");
        String pid = jsonObject.getString("pid");
        String commentFrom = jsonObject.getString("comment_from");
        String email = jsonObject.getString("email");
        String content = jsonObject.getString("content");

        Comment comment = new Comment();
        comment.setId(AccountUtil.getUUID());
        comment.setCommentNickname(nickname);
        comment.setCommentContent(content);
        comment.setCommentIp(IPUtils.getIP(request));
        comment.setCommentAddress(address);
        comment.setCommentEmail(email);
        comment.setCommentTime(DateTimeUtil.getCurrentDateTime());
        comment.setCommentFrom(commentFrom);
        comment.setCommentFor("");
        comment.setCommentFather(pid);
        comment.setCommentStatus(Constant.AUDIT_WAIT);
        comment.setCommentRemark("");
        comment.setIsDelete(Constant.UN_DELETE);

        boolean save = commentService.save(comment);
        if (save) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }


}
