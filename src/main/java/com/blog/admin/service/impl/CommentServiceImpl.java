package com.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.admin.config.Constant;
import com.blog.admin.dao.ArticleDao;
import com.blog.admin.dao.CommentDao;
import com.blog.admin.dto.CommentDTO;
import com.blog.admin.entity.Article;
import com.blog.admin.entity.Comment;
import com.blog.admin.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.druid.sql.visitor.SQLEvalVisitorUtils.eq;


@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

    @Autowired
    private CommentDao commentDao;
    @Autowired
    private ArticleDao articleDao;

    @Override
    public int getCount() {
        QueryWrapper<Comment> query = new QueryWrapper<>();
        query.eq("is_delete", Constant.UN_DELETE);
        return commentDao.selectCount(query);
    }

    @Override
    public Map<String, Object> getTableComment(int current, int size, String select, String keyword) {
        Map<String, Object> res = new HashMap<>();
        QueryWrapper<Comment> query = new QueryWrapper<>();
        Page<Comment> pageConfig = new Page<>(current, size);

        query.select("id", "comment_nickname", "comment_content", "comment_ip", "comment_address", "comment_email", "comment_time", "comment_from", "comment_for", "comment_father", "comment_status", "comment_remark").eq("is_delete", Constant.UN_DELETE);
        //留言板
        if (Constant.COMMENT_FROM_MSGBOARD.equals(select)) {
            query.eq("comment_from", Constant.COMMENT_FROM_MSGBOARD);
            //文章评论
        } else if (Constant.COMMENT_FROM_ARTICLE.equals(select)) {
            query.eq("comment_from", Constant.COMMENT_FROM_ARTICLE);
        }
        if (!"".equals(keyword)) {
            query.like("comment_content", keyword).or().like("comment_address", keyword).or().like("comment_nickname", keyword);
        }
        query.orderByDesc("comment_time");
        IPage<Comment> commentIPage = commentDao.selectPage(pageConfig, query);

        List<Comment> records = commentIPage.getRecords();
        for (Comment record : records) {
            //设置comment_for是留言板还是文章的标题
            if ("1".equals(record.getCommentFrom())) {
                record.setCommentForTitle("留言板");
            } else {
                String articleId = record.getCommentFor();
                Article article = articleDao.selectOne(new QueryWrapper<Article>().select("article_title").eq("ID", articleId));
                record.setCommentForTitle(article.getArticleTitle());
            }

            //有父级评论设置父级评论
            if (!"".equals(record.getCommentFather())) {
                Comment comment = commentDao.selectOne(new QueryWrapper<Comment>().select("comment_nickname", "comment_content").eq("ID", record.getCommentFather()));
                record.setCommentFatherNickname(comment.getCommentNickname());
                record.setCommentFatherContent(comment.getCommentContent());
            }
        }

        res.put("pageCode", commentIPage.getCurrent());
        res.put("pageSize", commentIPage.getSize());
        res.put("records", records);
        res.put("totalPage", commentIPage.getTotal());

        return res;
    }

    @Override
    public List<CommentDTO> getArticleComment(int id) {
        QueryWrapper<Comment> query = new QueryWrapper<>();
        query.select("ID", "comment_nickname", "comment_content", "comment_time", "comment_father")
                .eq("comment_from", Constant.COMMENT_FROM_ARTICLE).eq("comment_for", id)
                .eq("is_delete", Constant.UN_DELETE).eq("comment_status", Constant.AUDIT_PASS)
                .orderByDesc("comment_time");
        //结果集
        List<Comment> rs = commentDao.selectList(query);
        return buildComment(rs);
    }

    @Override
    public List<CommentDTO> getMsgboardComment() {
        QueryWrapper<Comment> query = new QueryWrapper<>();
        query.select("ID", "comment_nickname", "comment_content", "comment_time", "comment_father")
                .eq("is_delete", Constant.UN_DELETE).eq("comment_status", Constant.AUDIT_PASS)
                .eq("comment_from", Constant.COMMENT_FROM_MSGBOARD).orderByDesc("comment_time");
        List<Comment> rs = commentDao.selectList(query);
        return buildComment(rs);
    }


    /**
     * 构建评论列表
     * @param list
     * @return
     */
    private List<CommentDTO> buildComment(List<Comment> list) {
        //将结果集转换成CommentDTO类型
        List<CommentDTO> rsDto = new ArrayList<>();
        for (Comment r : list) {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setComment(r);
            rsDto.add(commentDTO);
        }

        //获取所有根节点
        List<CommentDTO> rootNode = new ArrayList<>();
        for (CommentDTO commentDTO : rsDto) {
            if (commentDTO.getComment().getCommentFather() == null || "".equals(commentDTO.getComment().getCommentFather())) {
                rootNode.add(commentDTO);
            }
        }

        //获取根节点下的子节点
        List<CommentDTO> res = new ArrayList<>();
        for (CommentDTO commentDTO : rootNode) {
            CommentDTO commentDTO1 = buildTree(commentDTO.getComment(), rsDto);
            res.add(commentDTO1);
        }
        return res;
    }

    /**
     * 递归构建评论列表
     *
     * @param list
     * @return
     */
    private CommentDTO buildTree(Comment comment, List<CommentDTO> list) {
        //获取当前节点对象
        CommentDTO node = new CommentDTO();
        //查询id下的所有子节点
        List<CommentDTO> childNodes = new ArrayList<>();
        for (CommentDTO commentDTO : list) {
            if (commentDTO.getComment().getId().equals(comment.getId())) {
                node = commentDTO;
            }
            if (commentDTO.getComment().getCommentFather().equals(comment.getId())) {
                childNodes.add(commentDTO);
            }
        }

        for (CommentDTO child : childNodes) {
            CommentDTO commentDTO = buildTree(child.getComment(), list);
            commentDTO.getComment().setCommentFatherNickname(comment.getCommentNickname());
            commentDTO.getComment().setCommentFatherContent(comment.getCommentContent());
            node.getNodes().add(commentDTO);
        }

        return node;
    }
}
