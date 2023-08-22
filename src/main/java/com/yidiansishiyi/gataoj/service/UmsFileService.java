package com.yidiansishiyi.gataoj.service;

import com.yidiansishiyi.gataoj.model.entity.UmsFile;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zeroc
* @description 针对表【ums_file(图片)】的数据库操作Service
* @createDate 2023-08-22 15:09:41
*/
public interface UmsFileService extends IService<UmsFile> {
    UmsFile get(String md5,Long size,String suffix);
}
