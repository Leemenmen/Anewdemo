package com.newdmsp.demo.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.Value;

@Data
@ToString
@ApiModel(value = "数据集")
public class DataSet {
    @ApiModelProperty(value = "数据集id")
    private Integer id;
    @ApiModelProperty(value = "实验id")
    private Integer expId;
    @ApiModelProperty(value = "实验名称")
    private String expName;
    @ApiModelProperty(value = "数据集路径")
    private String dataPath;
    @ApiModelProperty(value = "教师id")
    private Integer tid;
    @ApiModelProperty(value = "学生id")
    private Integer sid;
    @ApiModelProperty(value = "创建时间")
    private String creatime;

    private String expname;
}
