package com.atguigu.scw.project.vo.request;

import com.atguigu.scw.common.vo.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author erdong
 * @create 2019-10-08 21:13
 */
@Data
public class ProjectStepThreeInfoVo extends BaseVo {

    @ApiModelProperty("企业收款账号")
    private String alipayAccount; //企业收款账号

    @ApiModelProperty("法人身份证号")
    private String idcard;        //法人身份证号

    @ApiModelProperty("项目之前的临时token")
    private String projectToken;
}
