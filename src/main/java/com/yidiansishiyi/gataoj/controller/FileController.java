package com.yidiansishiyi.gataoj.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.dfa.WordTree;
import com.yidiansishiyi.gataoj.common.BaseResponse;
import com.yidiansishiyi.gataoj.common.BucketName;
import com.yidiansishiyi.gataoj.common.ErrorCode;
import com.yidiansishiyi.gataoj.common.ResultUtils;
import com.yidiansishiyi.gataoj.constant.FileConstant;
import com.yidiansishiyi.gataoj.exception.BusinessException;
import com.yidiansishiyi.gataoj.manager.CosManager;
import com.yidiansishiyi.gataoj.model.dto.file.UploadFileRequest;
import com.yidiansishiyi.gataoj.model.entity.User;
import com.yidiansishiyi.gataoj.model.enums.FileUploadBizEnum;
import com.yidiansishiyi.gataoj.service.UserService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yidiansishiyi.gataoj.service.impl.FileUploadServiceImpl;
import com.yidiansishiyi.gataoj.utils.MinioUtil;
import io.minio.errors.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URLEncoder;


import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



/**
 * 文件接口
 *
 * @author  sanqi
 *
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Value("${minio.url}")
    private String url;

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    @Autowired
    private MinioUtil minioUtil;

    /**
     * 文件上传
     *
     * @param multipartFile
     * @param uploadFileRequest
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile multipartFile,
            UploadFileRequest uploadFileRequest, HttpServletRequest request) {
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        if (fileUploadBizEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        validFile(multipartFile, fileUploadBizEnum);
        User loginUser = userService.getLoginUser(request);
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), loginUser.getId(), filename);
        File file = null;
        WordTree wordTree = new WordTree();
        try {
            // 上传文件
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filepath, file);
            // 返回可访问地址
            return ResultUtils.success(FileConstant.COS_HOST + filepath);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

    /**
     * 校验文件
     *
     * @param multipartFile
     * @param fileUploadBizEnum 业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
        }
    }


    @ApiOperation("上传一个文件")
    @PostMapping(value = "/uploadfile")
    public BaseResponse fileupload(@RequestPart("uploadfile") MultipartFile uploadfile, @RequestParam String bucket,
                                 @RequestParam(required=false) String objectName){
        try {
            minioUtil.createBucket(bucket);
            if (objectName != null) {
                minioUtil.uploadFile(uploadfile.getInputStream(), bucket, objectName+"/"+uploadfile.getOriginalFilename());
            } else {
                minioUtil.uploadFile(uploadfile.getInputStream(), bucket, uploadfile.getOriginalFilename());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResultUtils.success("上传成功");
    }

    @ApiOperation("列出所有的桶")
    @GetMapping(value = "/listBuckets")
    public BaseResponse listBuckets() throws Exception {
        return ResultUtils.success(minioUtil.listBuckets());
    }

    @ApiOperation("递归列出一个桶中的所有文件和目录")
    @GetMapping(value = "/listFiles")
    public BaseResponse listFiles(@RequestParam String bucket) {
        try {
            return ResultUtils.success( minioUtil.listFiles(bucket));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation("下载一个文件")
    @GetMapping(value = "/downloadFile")
    public void downloadFile(@RequestParam String bucket, @RequestParam String objectName,
                             HttpServletResponse response) {
        InputStream stream = null;
        ServletOutputStream output = null;
        try {
            stream = minioUtil.download(bucket, objectName);
            output = response.getOutputStream();
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(objectName.substring(objectName.lastIndexOf("/") + 1), "UTF-8"));
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            IOUtils.copy(stream, output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @ApiOperation("删除一个文件")
    @GetMapping(value = "/deleteFile")
    public BaseResponse deleteFile(@RequestParam String bucket, @RequestParam String objectName){
        try {
            minioUtil.deleteObject(bucket, objectName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResultUtils.success("删除成功");
    }

    @ApiOperation("删除一个桶")
    @GetMapping(value = "/deleteBucket")
    public BaseResponse deleteBucket(@RequestParam String bucket) {
        try {
            minioUtil.deleteBucket(bucket);
        } catch (Exception e) {
            log.error("删除错误",e.getMessage());
            throw new RuntimeException(e);
        }
        return ResultUtils.success("删除成功");
    }

    @ApiOperation("图片文件上传")
    @PostMapping("/geturl")
    public BaseResponse<String> geturl(@RequestPart("file")MultipartFile file) {
        String imgurl= null;
        try {
            String fileupload = minioUtil.fileupload(file, BucketName.Image);
            System.out.println(fileupload);
            imgurl = url+fileupload;
        } catch (Exception e) {
            log.error("文件上传错误",e.getMessage());
            throw new RuntimeException(e);
        }
        return ResultUtils.success(imgurl);

    }
}
