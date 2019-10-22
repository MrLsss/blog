package com.blog.admin.dto;


import com.blog.admin.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liushuai
 * 前台展示
 * 父级评论和它的子评论
 */
@Data
@AllArgsConstructor
public class CommentDTO {

    private Comment comment;

    private List<CommentDTO> nodes;

    public CommentDTO() {
        this.nodes = new ArrayList<>();
    }
}
