package com.atguigu.scw.user.controller;

import com.atguigu.scw.user.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author erdong
 * @create 2019-09-25 20:48
 */
@Api(tags = "测试Swagger的Controller")
@RestController  // RestController:表示此类的所有方法返回值类型都为json格式
public class HelloController {

    @ApiOperation(value = "这是一个hello方法,只是用来测试用滴")
    @GetMapping("/hello")
    public String helloController() {
        return ">>>>>>>>>";
    }

    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", required = true, dataTypeClass = String.class),  // 切记此处为 dataType,不是paramType
            @ApiImplicitParam(name = "password", required = false, defaultValue = "123456")
    })
    @ApiOperation(value = "登录方法 post请求")
    @PostMapping("/login")
    public User login(String username, String password) {
        User user = new User(999, username, password, "158@163.com");
        return user;
    }

    @ApiOperation(value = "这是一个Get请求方法")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "name", required = true, dataType = "String"),
            @ApiImplicitParam(name = "age", required = true, dataTypeClass = Integer.class) // dataTypeClass用引用类型的class
    })
    @GetMapping("/test")
    public String testChecks(String name, Integer age) {
        return "姓名: " + name + " 年龄：" + age;
    }


}
