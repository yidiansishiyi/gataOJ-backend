package com.yidiansishiyi.gataoj.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.yidiansishiyi.gataoj.common.BucketName;
import com.yidiansishiyi.gataoj.model.entity.Fileinfo;
import com.yidiansishiyi.gataoj.model.entity.UmsFile;
import com.yidiansishiyi.gataoj.service.UmsFileService;
import io.minio.errors.*;
import org.apache.commons.io.FilenameUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Component
public class MinioUtil {
	@Value("${minio.url}")
	private  String url;
	@Value("${minio.accessKey}")
	private  String  accessKey;
	@Value("${minio.secretKey}")
	private String secretKey;
	@Resource
    private MinioClient minioClient;

	@Resource
	UmsFileService umsFileService;
	
	/**
	 * 创建一个桶
	 */
	public void createBucket(String bucket) throws Exception {
		boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
		if (!found) {
			minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
		}
	}
	
	/**
	 * 上传一个文件
	 */
	public void uploadFile(InputStream stream, String bucket, String objectName) throws Exception {
		minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(objectName)
				.stream(stream, -1, 10485760).build());
	}
	
	/**
	 * 列出所有的桶
	 */
	public List<String> listBuckets() throws Exception {
		List<Bucket> list = minioClient.listBuckets();
		List<String> names = new ArrayList<>();
		list.forEach(b -> {
			names.add(b.name());
		});
		return names;
	}
	
	/**
	 * 列出一个桶中的所有文件和目录
	 */
	public List<Fileinfo> listFiles(String bucket) throws Exception {
		Iterable<Result<Item>> results = minioClient.listObjects(
			    ListObjectsArgs.builder().bucket(bucket).recursive(true).build());
			
			List<Fileinfo> infos = new ArrayList<>();
				results.forEach(r->{
					Fileinfo info = new Fileinfo();
					try {
						Item item = r.get();
						info.setFilename(item.objectName());
						info.setDirectory(item.isDir());
						infos.add(info);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
		return infos;
	}
	
	/**
	 * 下载一个文件
	 */
	public InputStream download(String bucket, String objectName) throws Exception {
		InputStream stream = minioClient.getObject(
		              GetObjectArgs.builder().bucket(bucket).object(objectName).build());
		return stream;
	}
	
	/**
	 * 删除一个桶
	 */
	public void deleteBucket(String bucket) throws Exception {
		minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
	}
	
	/**
	 * 删除一个对象
	 */
	public void deleteObject(String bucket, String objectName) throws Exception {
		minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
	}


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
