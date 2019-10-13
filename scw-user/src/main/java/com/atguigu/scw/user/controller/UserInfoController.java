package com.atguigu.scw.user.controller;

import com.atguigu.scw.user.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author erdong
 * @create 2019-09-26 19:14
 */
@Api(value = "用户信息请求控制层")  // 表示对此类的说明注解
@RestController                   // 这个注解代表此类的返回值类型都是json格式的
public class UserInfoController {

    @ApiOperation("获取个人信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", required = true, dataTypeClass = Integer.class)
    })
    @GetMapping("/user/info/query")
    public User getUserInfo(Integer id) {
        User user = new User();
        user.setId(1);
        user.setId(2);
        if (user.getId() == 1) {
            user.setPassword("erdong96");
            user.setUsername("Summer");
            return user;
        }
        if (user.getId() == 2) {
            user.setUsername("Summer_morning");
            user.setPassword("erdong28");
            return user;
        }
        return null;
    }

    @ApiOperation("更新个人信息")
    @ApiImplicitParams(value = {
            //@ApiImplicitParam(name = "user", required = true, dataTypeClass = User.class)
    })
    @PostMapping("/user/info/update")
    public User updateUserInfo(User user) {
        return user;
    }

    @ApiOperation("获取用户收获地址")
    @ApiImplicitParam(name = "id", required = true, dataTypeClass = Integer.class)
    @GetMapping("/user/info/address/query")
    public String getAddress(Integer id) {
        return "北京中关村888号" + id;
    }

    @ApiOperation("新增用户收货地址")
    @ApiImplicitParams(value = {
            //@ApiImplicitParam(name = "user", required = true, dataTypeClass = User.class),
            @ApiImplicitParam(name = "address", required = true, dataTypeClass = String.class)
    })
    @PostMapping("/user/info/address/save")
    public String saveAddress(User user, String address) {
        return "ok:" + user + "收获地址：" + address;
    }

    @ApiOperation("查看我的订单")
    @ApiImplicitParams(value = {
            //@ApiImplicitParam(name = "user", required = true, dataTypeClass = User.class),
            @ApiImplicitParam(name = "id", required = true, dataTypeClass = Integer.class)
    })
    @GetMapping("/user/info/order/query")
    public String getUserInfo(User user, Integer id) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return "ok:" + user + id + ",详情订单编号：" + uuid;
    }

    @ApiOperation("新增用户订单信息")
    //@ApiImplicitParam(name = "user", required = true, dataTypeClass = User.class)
    @PostMapping("/user/info/order/save")
    public String saveUserInfo(User user) {
        return "ok;" + user;
    }

}