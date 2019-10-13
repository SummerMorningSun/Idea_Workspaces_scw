package com.atguigu.scw.user.controller;

import com.alibaba.fastjson.JSON;
import com.atguigu.scw.common.consts.AppConsts;
import com.atguigu.scw.common.templates.SmsTemplate;
import com.atguigu.scw.common.utils.AppResponse;
import com.atguigu.scw.common.utils.ScwUtils;
import com.atguigu.scw.user.bean.TMember;
import com.atguigu.scw.user.vo.response.MemberResponseVo;
import com.atguigu.scw.user.vo.service.MemberService;
import com.atguigu.scw.user.vo.request.MemberRequestVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author erdong
 * @create 2019-09-27 15:46
 */
@Api("处理用户注册验证码登录请求的controller")
@RestController
@Slf4j
public class UserController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    SmsTemplate smsTemplate;
    @Autowired
    MemberService memberService;

    // 3.处理登录请求的方法
    @ApiOperation("处理登录请求的方法")
    @PostMapping("/user/doLogin")
    public AppResponse<Object> toLogin(String loginacct, String password) {
        // 3.1 调用逻辑层查询登录的用户信息 （是否存在）
        TMember member = memberService.getMember(loginacct, password);
        if (member == null) {
            // 登录失败
            return AppResponse.fail(null, "用户名密码错误！");
        }
        // 3.2 查询成功，创建存储信息的键，将用户信息存到redis中
        // 设置 随机token
        String memberToken = UUID.randomUUID().toString().replace("-", "");
        // 将对象转换为json
        String memberJson = JSON.toJSONString(member);
        stringRedisTemplate.opsForValue().set(memberToken, memberJson, 7, TimeUnit.DAYS); // 设置可以登录存储在redis中7天有效
        // 3.3 返回token给前台系统
        MemberResponseVo responseVo = new MemberResponseVo();
        BeanUtils.copyProperties(memberToken, responseVo);
        responseVo.setAccessToken(memberToken);
        // 以后前台系统访问后台系统时，只要携带token,能够在redis中获取用户信息，就代表可以登录
        return AppResponse.ok(responseVo, "登录成功");
    }


    // 2. 处理注册请求的方法
    @ApiOperation("处理注册请求的方法")
    @PostMapping("/user/doRegist")
    public AppResponse<Object> doRegist(MemberRequestVo vo) {
        // 2.1 检测验证码是否正确
        // 查询手机号对应的验证码
        String loginacct = vo.getLoginacct();
        String redisCode = stringRedisTemplate.opsForValue().get(AppConsts.CODE_PREFIX + loginacct + AppConsts.CODE_CODE_SUFFIX);
        if (StringUtils.isEmpty(redisCode)) {
            return AppResponse.fail(null, "验证码过期");
        }
        if (!redisCode.equals(vo.getCode())) {
            return AppResponse.fail(null, "验证码错误");
        }
        // 2.2 注册
        memberService.saveMember(vo);

        // 2.3 删除redis中的验证码
        stringRedisTemplate.delete(AppConsts.CODE_PREFIX + loginacct + AppConsts.CODE_CODE_SUFFIX);
        return AppResponse.ok(null, "注册成功");
    }

    // 1. 给手机发送短信验证码的方法
    @ApiOperation("发送验证码的方法")
    @ApiImplicitParams(value = @ApiImplicitParam(name = "phoneCode", required = true, value = "手机号码"))
    @PostMapping("/user/sendSms")
    public AppResponse<Object> sendSms(@RequestParam("phoneCode") String phoneCode) {
        boolean b = ScwUtils.isMobilePhone(phoneCode);
        if (!b) {
            return AppResponse.fail(null, "手机号码格式错误！");
        }
        // 验证redis中存储的当前手机号码获取验证码的次数【第一次获取没有，或者没有超过指定次数可以继续获取验证码】
        // 一个手机号码一天内最多只能获取3次验证码：  code:phoneCode
        String countStr = stringRedisTemplate.opsForValue().get(AppConsts.CODE_PREFIX + phoneCode + AppConsts.CODE_COUNT_SUFFIX);
        int count = 0;
        if (!StringUtils.isEmpty(countStr)) {
            // 如果数量字符串不为空，转为数字
            count = Integer.parseInt(countStr);
        }
        if (count > 3) {
            return AppResponse.fail(null, "获取验证码超过3次");
        }
        // 验证redis中当前手机号码是否存在未通过验证码【看需求是否编写】
        // 获取当前手机号码在redis中的验证码：如果为空，代表没有： code:23424:codestr
        // 键在值在，键亡人亡
        Boolean hasKey = stringRedisTemplate.hasKey(AppConsts.CODE_PREFIX + phoneCode + AppConsts.CODE_CODE_SUFFIX);
        if (hasKey) {
            return AppResponse.fail(null, "一天最多获取验证码3次哦,请不要频繁获取验证码哦");
        }
        // 发送验证码。
        // 随机6位生成验证码  带字母的
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        // 封装发送短信验证码请求参数的集合
        Map<String, String> querys = new HashMap<>();
        querys.put("mobile", phoneCode);
        querys.put("param", AppConsts.CODE_PREFIX + code);
        querys.put("tpl_id", "TP1711063");
        /*Boolean sendSms = smsTemplate.sendSms(querys);
        if (!sendSms) {
            return "短信验证码发送失败";
        }*/
        // 将验证码存到redis中5分钟
        stringRedisTemplate.opsForValue().set(AppConsts.CODE_PREFIX + phoneCode + AppConsts.CODE_CODE_SUFFIX, code, 5, TimeUnit.MINUTES);
        // 修改该收号码发送验证码的次数
        Long expire = stringRedisTemplate.getExpire(AppConsts.CODE_PREFIX + phoneCode + AppConsts.CODE_CODE_SUFFIX, TimeUnit.SECONDS);// 获取过期时间
        log.info("查询到过期时间:{}", expire); // -2代表已过期(还未注册过)，（已过期）
        if (expire == null || expire <= 0) {
            expire = (long) (24 * 60);
        }
        count++;
        stringRedisTemplate.opsForValue().set(AppConsts.CODE_PREFIX + phoneCode + AppConsts.CODE_COUNT_SUFFIX, count + "", expire, TimeUnit.MINUTES);
        return AppResponse.ok(null, "发送验证码成功");
    }
}
