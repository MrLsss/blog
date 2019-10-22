package com.blog.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.admin.dto.CommentDTO;
import com.blog.admin.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService extends IService<Comment> {

    /**
     * 获取留言总数
     * @return
     */
    int getCount();

    /**
     * 评论管理表格数据
     * classify 用于下拉框选择
     * keyword 用于搜索
     * @return
     */
    Map<String, Object> getTableComment(int current, int size, String select, String keyword);


    /**
     * 获取文章下的所有评论
     * @return
     */
    List<CommentDTO> getArticleComment(int id);

    /**
     * 获取留言板数据
     * @return
     */
    List<CommentDTO> getMsgboardComment();

}
