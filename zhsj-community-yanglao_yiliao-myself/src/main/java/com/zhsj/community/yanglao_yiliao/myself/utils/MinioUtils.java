package com.zhsj.community.yanglao_yiliao.myself.utils;

import com.zhsj.basecommon.enums.ErrorEnum;
import com.zhsj.basecommon.exception.BaseException;
import com.zhsj.community.yanglao_yiliao.myself.config.MinionConfig;
import io.minio.MinioClient;
import io.minio.policy.PolicyType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
public class MinioUtils {

	//ip
	private static final String ENDPOINT = MinionConfig.endPoint;
	//端口
	private static final int PROT = MinionConfig.port;
	//ACCESS_KEY
	private static final String ACCESSKEY = MinionConfig.accessKey;
	//SECRET_KEY
	private static final String SECRETKET = MinionConfig.secretKey;
	//存储桶名称
	private static String BUCKETNAME = null;

	private static volatile MinioClient minioClient = null;


	/**
	 * 文件上传
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String upload(MultipartFile file, String bucketName) {
		try {
			//获取minio客户端实例
			minioClient = getMinioClientInstance();
			//创建存储桶
			createBucket(bucketName);
			// 文件存储的目录结构
			if(file == null){
				throw new BaseException(ErrorEnum.FAIL,"请上传文件");
			}
			String endName;
			String objectName;
			if (!StringUtils.isEmpty(file.getOriginalFilename())) {
				endName = file.getOriginalFilename();
				objectName = getRandomFileName(endName);
			}else{
				objectName = getRandomFileName("");
			}
			// 存储文件
			minioClient.putObject(BUCKETNAME, objectName, file.getInputStream(), file.getContentType());
			//返回路径
			return ENDPOINT + ":" + PROT + "/" + BUCKETNAME + "/" + objectName;
		} catch (Exception e) {
			throw new BaseException(ErrorEnum.FAIL,"上传失败,MinioUtils.upload()方法出现异常：" + e.getMessage());
		}
	}

	private static  String getRandomFileName(String fileName){
		return UUID.randomUUID().toString().replace("-","");
	}

	/**
	 * 根据存储名称创建存储桶目录
	 * @param bucketName 		文件目录名称
	 */
	private static void createBucket(String bucketName){
		try {
			minioClient = getMinioClientInstance();
			// 存储桶
			BUCKETNAME = bucketName;
			//存入bucket不存在则创建
			if (!minioClient.bucketExists(BUCKETNAME)) {
				minioClient.makeBucket(BUCKETNAME);
				minioClient.setBucketPolicy(BUCKETNAME, "*", PolicyType.READ_WRITE);
			}
		} catch (Exception e) {
			log.error("com.jsy.community.utils.MinioUtils.createBucket：{}", "创建存储目录失败!:"+e.getMessage());
			throw new BaseException(ErrorEnum.FAIL,e.getMessage());
		}
	}


	/**
	 *  懒加载当用到 MinioClient 时 创建实例，只创建一次
	 * @author YuLF
	 * @since  2020/12/9 17:29
	 * @return			返回MinioClient
	 */
	private static MinioClient getMinioClientInstance() throws Exception {
		if(minioClient == null){
			synchronized (MinioUtils.class){
				if(minioClient == null){
					minioClient = new MinioClient(ENDPOINT, PROT, ACCESSKEY, SECRETKET);
				}
			}
		}
		return minioClient;
	}

}
