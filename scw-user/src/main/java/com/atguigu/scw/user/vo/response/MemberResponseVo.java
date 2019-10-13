package com.atguigu.scw.user.vo.response;

import com.atguigu.scw.common.vo.BaseVo;
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
@Api(value = "处理登录请求的参数类")
public class MemberResponseVo extends BaseVo {

    @ApiModelProperty("手机号码")
    private String loginacct; //手机号码
    @ApiModelProperty("用户姓名")
    private String username;// 用户姓名
    //@ApiModelProperty("控制用户登录的令牌")
    //private String accessToken; // 控制用户登录的令牌
    @ApiModelProperty("邮箱")
    private String email; //邮箱
    @ApiModelProperty("用户的类型")
    private String usertype; //用户的类型    0- 个人  1-企业

}
