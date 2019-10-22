package com.blog.admin.dto;

import com.blog.admin.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * @author liushuai
 * 文章归档
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleArchive implements Serializable {

    private String date;

    private List<Article> articleList;

}
