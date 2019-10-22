package com.blog.admin.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "t_blog_sitestatistics")
public class SiteStatistics implements Serializable {

    @TableId(value = "ID", type = IdType.AUTO)
    private int Id;

    @TableField(value = "ss_article_counts")
    private int ssArticleCounts;

    @TableField(value = "ss_tag_counts")
    private int ssTagCounts;

    @TableField(value = "ss_classify_counts")
    private int ssClassifyCounts;

    @TableField(value = "ss_comment_counts")
    private int ssCommentCounts;

    @TableField(value = "ss_run_days")
    private int ssRunDays;

    @TableField(value = "ss_last_updatetime")
    private String ssLastUpdateTime;
}
