package com.blog.admin.dao;

import com.blog.admin.common.dao.CommonMapper;
import com.blog.admin.entity.WebMasterInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author liushuai
 */
@Repository
public interface WebMasterInfoDao extends CommonMapper<WebMasterInfo> {

    int findPwdByAccount(@Param("account") String account, @Param("password") String password);

}
