package com.blog.admin.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.admin.config.Constant;
import com.blog.admin.config.LoginRequired;
import com.blog.admin.config.RecordLog;
import com.blog.admin.dto.Result;
import com.blog.admin.entity.Comment;
import com.blog.admin.entity.WebMasterInfo;
import com.blog.admin.service.CommentService;
import com.blog.admin.service.WebMasterInfoService;
import com.blog.admin.utils.AccountUtil;
import com.blog.admin.utils.DateTimeUtil;
import com.blog.admin.utils.IPUtils;
import com.blog.admin.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/admin")
@RestController
public class CommentController {
    /**
     * 删除父评论，要修改相应的子评论
     */

    @Autowired
    private CommentService commentService;
    @Autowired
    private WebMasterInfoService infoServcie;

    private WebMasterInfo getInfo() {
        return infoServcie.getOne(new QueryWrapper<WebMasterInfo>().select("wm_name", "wm_email", "wm_account"));
    }


    @GetMapping("/comments")
    @LoginRequired
    public Result getCommentPage(@RequestParam(value = "currentPage") int currentPage, @RequestParam(value = "size") int size, @RequestParam(value = "select", required = false) String select, @RequestParam(value = "keyword", required = false) String keyword) {
        return Result.SUCCESS(commentService.getTableComment(currentPage, size, select, keyword));
    }

    @RecordLog("审核评论")
    @PatchMapping("/comment")
    @LoginRequired
    public Result auditComment(@RequestBody String data) {
        JSONObject jsonObject = JsonUtil.String2JSON(data);

        String radio = jsonObject.getString("radio");
        String commentRemark = jsonObject.getString("commentRemark");
        String id = jsonObject.getString("id");

        Comment comment = new Comment();
        if (Constant.IS_ALLOW.equals(radio)) {
            comment.setCommentStatus(Constant.AUDIT_PASS);
        } else {
            comment.setCommentStatus(Constant.AUDIT_NOT_PASS);
            comment.setCommentRemark(commentRemark);
        }
        comment.setId(id);
        boolean b = commentService.updateById(comment);

        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    /**
     * 博主回复
     *
     * @param data
     * @param request
     * @return
     */
    @RecordLog("回复评论")
    @PostMapping("/comment")
    @LoginRequired
    public Result replyComment(@RequestBody String data, HttpServletRequest request) {
        JSONObject jsonObject = JsonUtil.String2JSON(data);
        String id = jsonObject.getString("id");
        String replyContent = jsonObject.getString("replyContent");
        String address = jsonObject.getString("address");
        String from = jsonObject.getString("from");
        String forWhere = jsonObject.getString("for");

        Comment comment = new Comment();
        comment.setId(AccountUtil.getUUID());
        comment.setCommentNickname(getInfo().getWmAccount());
        comment.setCommentContent(replyContent);
        comment.setCommentIp(IPUtils.getIP(request));
        comment.setCommentAddress(address);
        comment.setCommentEmail(getInfo().getWmEmail());
        comment.setCommentTime(DateTimeUtil.getCurrentDateTime());
        comment.setCommentFrom(from);
        if (Constant.COMMENT_FROM_MSGBOARD.equals(from)) {
            comment.setCommentFor("");
        } else {
            comment.setCommentFor(forWhere);
        }
        comment.setCommentFather(id);
        comment.setCommentStatus(Constant.AUDIT_PASS);
        comment.setIsDelete(Constant.UN_DELETE);

        boolean save = commentService.save(comment);
        if (save) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    @RecordLog("删除评论")
    @DeleteMapping("/comment/{id}")
    @LoginRequired
    public Result deleteComment(@PathVariable String id) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setIsDelete(Constant.IS_DELETE);

        boolean b = commentService.updateById(comment);
        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    @RecordLog("批量删除评论")
    @DeleteMapping("/comments/{ids}")
    @LoginRequired
    public Result batchDeleteComment(@PathVariable String[] ids) {
        List<Comment> list = new ArrayList<>();
        for (String id : ids) {
            Comment comment = new Comment();
            comment.setId(id);
            comment.setIsDelete(Constant.IS_DELETE);

            list.add(comment);
        }
        boolean b = commentService.updateBatchById(list);
        if (b) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

}
