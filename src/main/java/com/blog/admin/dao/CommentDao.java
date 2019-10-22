package com.blog.admin.dao;

import com.blog.admin.common.dao.CommonMapper;
import com.blog.admin.entity.Comment;
import org.springframework.stereotype.Repository;

/**
 * @author liushuai
 */
@Repository
public interface CommentDao extends CommonMapper<Comment> {
}
