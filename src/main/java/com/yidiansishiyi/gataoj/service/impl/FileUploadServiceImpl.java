package com.yidiansishiyi.gataoj.service.impl;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.yidiansishiyi.gataoj.common.BucketName;
import com.yidiansishiyi.gataoj.model.entity.UmsFile;
import com.yidiansishiyi.gataoj.service.FileUploadInterfaceService;
import com.yidiansishiyi.gataoj.service.UmsFileService;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class FileUploadServiceImpl implements FileUploadInterfaceService {
    @Value("${minio.url}")
    private  String url;
    @Value("${minio.accessKey}")
    private  String  accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;
    //
    @Autowired
    UmsFileService umsFileService;
    @Override
    public String fileupload(MultipartFile file, BucketName bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        System.out.println(url +" " + accessKey +" "+secretKey );
        String md5= DigestUtils.md5DigestAsHex(file.getInputStream());
        long size = file.getSize();
        String suffix=FilenameUtils.getExtension(file.getOriginalFilename());
        UmsFile umsFile = umsFileService.get(md5, size, suffix);
        if(!StringUtils.isEmpty(umsFile)){
            return  umsFile.getPath();
        }

        MinioClient minioClient = MinioClient.builder().endpoint(url).credentials(accessKey,secretKey).build();

        boolean img = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName.getName()).build());

        if(!img){
             throw new RuntimeException("当前容器不存在");
        }

        //文件名字重新命名，避免重复
        StringBuilder filename=new StringBuilder();
        filename.append( NanoIdUtils.randomNanoId());
        filename.append("."+ FilenameUtils.getExtension(file.getOriginalFilename()));

        //文件服务器中不存在 进行添加
        PutObjectArgs args = PutObjectArgs.builder().
                //文件名字
                        object(filename.toString()).
                //文件格式
                        contentType(file.getContentType()).
                //目标存储再minio的容器名字
                        bucket(bucketName.getName()).
                //通过流的方式
                        stream(file.getInputStream(), size, 0)
                .build();
        minioClient.putObject(args);

        //在文件服务器存储当前路径信息
        String id = NanoIdUtils.randomNanoId();
        String str="/"+bucketName.getName()+"/"+ filename;
        UmsFile umsfile = new UmsFile(id, md5, size, suffix, str);

        umsFileService.save(umsfile);

        return  umsfile.getPath();
    }
}
