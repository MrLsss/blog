package com.blog.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@TableName(value = "t_blog_article")
public class Article implements Serializable {

    @TableId(value = "ID", type = IdType.AUTO)
    private int Id;

    @TableField(value = "article_title")
    private String articleTitle;

    @TableField(value = "article_author")
    private String articleAuthor;

    @TableField(value = "article_tag_id")
    private String articleTagID;

    private List<Map<String, Object>> articleTag;

    @TableField(value = "article_content")
    private String articleContent;

    @TableField(value = "article_dec")
    private String articleDec;

    @TableField(value = "article_classify_id")
    private String articleClassifyID;

    private String articleClassify;

    @TableField(value = "is_main")
    private String isMain;

    @TableField(value = "is_own")
    private String isOwn;

    @TableField(value = "article_from")
    private String articleFrom;

    @TableField(value = "is_rec")
    private String isRec;

    @TableField(value = "article_view")
    private String articleView;

    @TableField(value = "article_comment")
    private String articleComment;

    @TableField(value = "article_creation_time")
    private String articleCreationTime;

    @TableField(value = "article_last_update_time")
    private String articleLastUpdateTime;

    @TableField(value = "article_status")
    private String articleStatus;

    @TableField(value = "article_content_type")
    private String articleContentType;

    @TableField(value = "article_cover_img")
    private String articleCoverImg;

    @TableField(value = "is_delete")
    private String isDelete;

}
