package com.blog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.admin.entity.WebMasterInfo;

public interface LoginService extends IService<WebMasterInfo> {


    /**
     * 登录
     * @return
     */
    boolean login(String account, String password);

}
