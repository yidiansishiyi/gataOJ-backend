package com.yidiansishiyi.gataoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yidiansishiyi.gataoj.model.entity.UmsFile;
import com.yidiansishiyi.gataoj.service.UmsFileService;
import com.yidiansishiyi.gataoj.mapper.UmsFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author zeroc
* @description 针对表【ums_file(图片)】的数据库操作Service实现
* @createDate 2023-08-22 15:09:41
*/
@Service
public class UmsFileServiceImpl extends ServiceImpl<UmsFileMapper, UmsFile>
    implements UmsFileService{

    @Resource
    private UmsFileMapper umsFileMapper;

    @Override
    public UmsFile get(String md5, Long size, String suffix) {
        QueryWrapper<UmsFile> umsFileQueryWrapper = new QueryWrapper<>();
        umsFileQueryWrapper.eq("md5",md5).eq("size",size).eq("suffix",suffix);
        UmsFile umsFile = umsFileMapper.selectOne(umsFileQueryWrapper);
        return umsFile;
    }
}




