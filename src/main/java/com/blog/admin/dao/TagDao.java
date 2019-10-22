package com.blog.admin.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.admin.common.dao.CommonMapper;
import com.blog.admin.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author liushuai
 */
@Repository
public interface TagDao extends CommonMapper<Tag> {

//    int selectCounts();

    /**
     * 适用于多表联查
     * @return
     */
//    IPage<Tag> selectPage(Page<Tag> page, @Param(Constants.WRAPPER) Wrapper<Tag> wrapper);

}
