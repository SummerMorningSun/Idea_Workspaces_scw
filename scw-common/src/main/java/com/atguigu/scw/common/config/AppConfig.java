package com.atguigu.scw.common.config;

import com.atguigu.scw.common.templates.OssTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author erdong
 * @create 2019-09-28 8:41
 */
@Configuration
public class AppConfig {

    // 注入oss模板类
    @ConfigurationProperties(prefix = "oss")
    @Bean
    public OssTemplate getOssTemplate() {
        return new OssTemplate();
    }


    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
