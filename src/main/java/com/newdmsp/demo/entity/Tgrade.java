package com.newdmsp.demo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@ApiModel(value = "班级实体类")
public class Tgrade {
    @ApiModelProperty(value = "用户id")
    private Integer id; //用户id
    @ApiModelProperty(value = "班级id")
    private Integer gradeid;
    @ApiModelProperty(value = "实验id")
    private Integer expid;
    @ApiModelProperty(value = "班级名称")
    private String gname;
    private Integer geid;
}
