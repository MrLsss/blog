package com.blog.admin.service;

import java.util.List;
import java.util.Map;

/**
 * 后台首页service
 *
 * @author liushuai
 */
public interface AdminService {


    /**
     * 获取首页的数据统计数据
     *
     * @return
     */
    Map<String, Integer> getCount();


    /**
     * 获取首页的文章分类饼状图数据
     *
     * @return
     */
    List<Map<String, Object>> getPie();

    /**
     * 获取文章访问top10柱状图的数据
     * @return
     */
    List<Map<String, Object>> getTOP10();

    /**
     * 获取标签云
     * @return
     */
    List<Map<String, Object>> getTagCloud();

    /**
     * 获取操作时间线
     */
    List<Map<String, Object>> getOperationTime();
}
