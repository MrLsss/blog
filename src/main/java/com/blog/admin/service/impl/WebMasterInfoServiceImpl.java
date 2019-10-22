package com.blog.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.admin.dao.WebMasterInfoDao;
import com.blog.admin.entity.WebMasterInfo;
import com.blog.admin.service.WebMasterInfoService;
import org.springframework.stereotype.Service;


@Service("webMasterInfoService")
public class WebMasterInfoServiceImpl extends ServiceImpl<WebMasterInfoDao, WebMasterInfo> implements WebMasterInfoService {
        }
