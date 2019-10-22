package com.blog.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.admin.dao.WebMasterInfoDao;
import com.blog.admin.entity.WebMasterInfo;
import com.blog.admin.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl extends ServiceImpl<WebMasterInfoDao, WebMasterInfo> implements LoginService {

    @Autowired
    private WebMasterInfoDao infoDao;


    @Override
    public boolean login(String account, String password) {
        int count = infoDao.findPwdByAccount(account, password);
        return count == 1;
    }
}
