package com.yidiansishiyi.gataoj.service;

import com.yidiansishiyi.gataoj.common.BucketName;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface FileUploadInterfaceService {
    String fileupload(MultipartFile multipartFile, BucketName Bucketsname) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

}
