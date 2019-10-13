package com.atguigu.scw;

import com.atguigu.scw.project.bean.TTag;
import com.atguigu.scw.project.mapper.TTagMapper;
import com.atguigu.scw.project.mapper.TTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
public class ScwProjectApplicationTests {

    @Autowired
    TTagMapper tagMapper;

    @Test
    public void contextLoads() throws FileNotFoundException {
        tagMapper.insertSelective(new TTag(null,0,"手机"));

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        /*String scheme = "http://";
        String endpoint = "oss-cn-shanghai.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAI4FnGw9M5p3qrv4fhbzHj";
        String accessKeySecret = "Nmevt0maUtPHVuc2G35EEi2zJU9N6a";
        String bukectName = "scw-oss-20190615";
        String imgsDir = "imgs/";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(scheme + endpoint, accessKeyId, accessKeySecret);
        File file = new File("C:\\Users\\erdong\\Desktop\\215.jpg");
        // 上传文件流。
        InputStream inputStream = new FileInputStream(file);
        String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replace("-", "") + "_" + file.getName();
        ossClient.putObject(bukectName, imgsDir + fileName, inputStream);
        // https://scw-oss-20190615.oss-cn-shanghai.aliyuncs.com/imgs/215.jpg
        log.info("图片上传地址：{}", scheme + bukectName + "." + endpoint + "/" + imgsDir + fileName);
        // http://bukectName+endpoint+imgsDir+/215.jpg
        // 关闭OSSClient。
        ossClient.shutdown();*/
    }

}
