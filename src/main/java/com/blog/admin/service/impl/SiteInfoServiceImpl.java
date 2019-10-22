package com.blog.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.admin.dao.SiteInfoDao;
import com.blog.admin.entity.SiteInfo;
import com.blog.admin.service.SiteInfoService;
import org.springframework.stereotype.Service;


@Service("siteInfoService")
public class SiteInfoServiceImpl extends ServiceImpl<SiteInfoDao, SiteInfo> implements SiteInfoService {
}
