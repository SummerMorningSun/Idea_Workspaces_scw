package com.atguigu.scw.common.vo;

import lombok.Data;

/**
 * 需要使用用accessToken的类的父类
 * @author erdong
 * @create 2019-10-07 9:54
 */
@Data
public class BaseVo {
    private String accessToken; // 用户登录成功的tonken,（我理解为令牌）
}
