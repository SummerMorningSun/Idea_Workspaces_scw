package com.atguigu.scw.common.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author erdong
 * @create 2019-09-27 16:04
 */
public class ScwUtils {

    //保存对象到redis中的方法
    public static<T> void saveBeanToRedis(T t ,StringRedisTemplate stringRedisTemplate , String key ,String prefix) {
        String jsonString = JSON.toJSONString(t);
        stringRedisTemplate.opsForValue().set(prefix+key, jsonString);
    }

    // 从redis中读取gson字符串并封装为指定类型对象的方法
    public static<T> T getBeanFromRedis(StringRedisTemplate redisTemplate, String key , Class<T> type){
        // 先查询json字符串
        String jsonStr = redisTemplate.opsForValue().get(key);
        // 再将字符串转为指定类型的对象
        if (StringUtils.isEmpty(jsonStr)){
            return null;
        }
        T t = JSON.parseObject(jsonStr, type);
        return t;
    }


    public static boolean isMobilePhone(String phone) {
        boolean flag=true;
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            flag= false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            flag = m.matches();
        }

        return flag;
    }

}
