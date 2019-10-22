package com.blog.admin.dao;

import com.blog.admin.common.dao.CommonMapper;
import com.blog.admin.entity.WebMasterInfo;
import org.springframework.stereotype.Repository;

/**
 * @author liushuai
 */
@Repository
public interface WebMasterInfoDao extends CommonMapper<WebMasterInfo> {

    int findPwdByAccount(String account, String password);

}
