package com.atguigu.scw;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.atguigu.scw.user.bean.TMember;
import com.atguigu.scw.user.mapper.TMemberMapper;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ScwUserApplicationTests {

    @Resource
    RedisTemplate<Object, Object> redisTemplate; // 操作redis的模板类，一般用来操作对象

    @Autowired
    StringRedisTemplate stringTemplate;
    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test2(){
        System.out.println("我的测试");
    }

    @Test
    public void contextLoads() {
        /*Boolean flag1 = redisTemplate.hasKey("key1");
        Boolean flag2 = redisTemplate.hasKey("aaa");
        logger.info("redis中key1键是否存在：{}", flag1);
        logger.info("redis中aaa键是否存在：{}", flag1);
        redisTemplate.opsForValue().set("key1", "xxx", 100, TimeUnit.SECONDS);
        String aaa = (String) redisTemplate.opsForValue().get("aaa");
        logger.info("===============" + aaa);
        Long expire = redisTemplate.getExpire("key1", TimeUnit.SECONDS);
        logger.info("redis中key1键的过期时间：{}", expire);*/
        //redisTemplate.delete("aaa");

        stringTemplate.opsForValue().set("key1", "code:286215", 1, TimeUnit.HOURS);
        Date date = new Date();
        // 转成json格式
        Gson gson = new Gson();
        String jsonDate = gson.toJson(date);
        // 存储到redis中
        stringTemplate.opsForValue().set("dateStr", jsonDate);
        // 获取存储到redis中的时间 dateStr
        String dateStr = stringTemplate.opsForValue().get("dateStr");
        // 从redis 中获取的数据是 json 格式的所以要用Gson转换成 时间类型的对象，并接收
        date = gson.fromJson(dateStr, Date.class);
        // 打印出时间验证时间 看看什么结果
        System.err.println(date);
    }

    @Autowired
    TMemberMapper mappers;

    @Test
    public void test() {
        List<TMember> tMembers = mappers.selectByExample(null);
        System.out.println(tMembers);
    }
}