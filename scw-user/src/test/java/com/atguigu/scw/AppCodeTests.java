package com.atguigu.scw;

import com.atguigu.scw.common.templates.SmsTemplate;
import com.atguigu.scw.common.utils.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 产品验证码测试类
 *
 * @author erdong
 * @create 2019-09-27 14:04
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppCodeTests {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SmsTemplate smsTemplate;

    @Test
    public void testCode() {
        HashMap<String, String> querys = new HashMap<>();
        querys.put("mobile", "15890345670");
        querys.put("param", "code:erdongnihao");
        querys.put("tpl_id", "TP1711063");
        smsTemplate.sendSms(querys);
    }

    @Test
    public void testHttpClients() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet("http://www.baidu.com/");

            System.err.println("Executing request " + httpget.getRequestLine());

            // 响应结果解析工具类  匿名内部类
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity,"UTF-8") : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.err.println(responseBody);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            logger.info("==============1" + String.valueOf(e));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("==============2" + String.valueOf(e));
            }
        }
    }

}
