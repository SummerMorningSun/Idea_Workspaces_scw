package com.atguigu.scw.common.templates;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 上传图片类 OssTemplate
 *
 * @author erdong
 * @create 2019-09-29 20:41
 */
@Data
public class OssTemplate {


    // Endpoint以杭州为例，其它Region请按实际情况填写。
    String scheme;
    String endpoint;
    // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
    String accessKeyId;
    String accessKeySecret;
    String bukectName;
    String imgsDir;

    // 上传图片的方法，返回上传成功图片的路径:用户在项目发布页面中上传图片给服务器（MultipartFile 代表上传的一个文件）
    public String upLoadImg(MultipartFile multipartFile) throws FileNotFoundException {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(scheme + endpoint, accessKeyId, accessKeySecret);
        // 上传文件流。
        InputStream inputStream = null; //
        try {
            inputStream = multipartFile.getInputStream(); // 获取上传文件的文件流
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replace("-", "") + "_"+
                multipartFile.getOriginalFilename();
        ossClient.putObject(bukectName, imgsDir + fileName, inputStream);
        // https://scw-oss-20190615.oss-cn-shanghai.aliyuncs.com/imgs/215.jpg
        String path = scheme + bukectName + "." + endpoint + "/" + imgsDir + fileName;
        // http://bukectName+endpoint+imgsDir+/215.jpg
        // 关闭OSSClient。
        ossClient.shutdown();
        return path;
    }
}
