package com.atguigu.scw.user.vo.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author erdong
 * @create 2019-09-28 7:52
 */
@Data
@ToString
@Api(value = "处理注册请求的参数类")
public class MemberRequestVo {

    @ApiModelProperty("手机号码")
    private String loginacct; //手机号码
    @ApiModelProperty("密码")
    private String userpswd;//密码
    @ApiModelProperty("验证码")
    private String code; //验证码
    @ApiModelProperty("邮箱")
    private String email; //邮箱
    @ApiModelProperty("用户的类型")
    private String usertype; //用户的类型    0- 个人  1-企业

}
