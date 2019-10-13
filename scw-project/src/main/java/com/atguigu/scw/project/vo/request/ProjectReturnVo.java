package com.atguigu.scw.project.vo.request;

import com.atguigu.scw.common.vo.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author erdong
 * @create 2019-10-08 20:11
 */
@Data
public class ProjectReturnVo extends BaseVo {
    @ApiModelProperty("项目之前的临时token")
    private String projectToken;

    @ApiModelProperty(value="0虚拟回报 ， 1 实物回报")
    private String type;//回报类型

    @ApiModelProperty(value="支持金额")
    private Integer supportmoney;

    @ApiModelProperty(value="回报内容")
    private String content;

    @ApiModelProperty(value="总回报数量  0不限制回报数量")
    private Integer count;

    @ApiModelProperty(value="单笔限购  默认值1")
    private Integer signalpurchase;

    @ApiModelProperty(value="每个id的限购数量")
    private Integer purchase;

    @ApiModelProperty(value="邮费 0表示包邮")
    private Integer freight;

    @ApiModelProperty(value="发票  0不开发票  ， 1开发票")
    private String invoice;

    @ApiModelProperty(value="项目众筹成功后的回报时间")
    private Integer rtndate;
}
