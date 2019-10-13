package com.atguigu.scw.common.templates;

import com.atguigu.scw.common.utils.HttpUtils;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author erdong
 * @create 2019-09-27 15:17
 */
@Component
public class SmsTemplate implements Serializable {

    @Value("${sms.host}") // 只能在组件中使用
    private String host;
    @Value("${sms.path}")
    private String path;
    @Value("${sms.method}")
    private String method;
    @Value("${sms.appcode}")
    private String appcode;

    // 发送短信方法
    public Boolean sendSms(Map<String, String> querys) {
//        String host = "http://dingxin.market.alicloudapi.com";
//        String path = "/dx/sendSms";
//        String method = "POST";
//        String appcode = "00bf7136a94640afa7f08487b74b86d2";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        /*
        参数值每次调用都不一样，需要作为方法参数传入
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", "15890345670");
        querys.put("param", "code:erdongnihao");
        querys.put("tpl_id", "TP1711063");*/
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.err.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
