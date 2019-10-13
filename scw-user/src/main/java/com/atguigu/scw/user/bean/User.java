package com.atguigu.scw.user.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author erdong
 * @create 2019-09-26 9:58
 */
@AllArgsConstructor  // 生成有参构造器
@NoArgsConstructor   // 生成无参构造器
@Data                // 生成getter和setter方法
@ToString            // 生成toString方法
@ApiModel(value = "用户实体类")
public class User {

    @ApiModelProperty("用户主键")
    private Integer id;
    @ApiModelProperty("用户账号")
    private String username;
    @ApiModelProperty("用户密码")
    private String password;
    @ApiModelProperty("用户邮箱")
    private String email;

}
