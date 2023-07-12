package com.newdmsp.demo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.Value;

@Data
@ToString
@ApiModel(value = "实验实体类")
public class Exp {
    @ApiModelProperty(value = "实验id")
    private Integer id;
    @ApiModelProperty(value = "实验名称")
    private String expname;
    @ApiModelProperty(value = "实验目的")
    private String exptarget;
    @ApiModelProperty(value = "实验任务")
    private String expwork;
    @ApiModelProperty(value = "实例代码路径")
    private String expcodeurl;
    @ApiModelProperty(value = "上传者id")
    private Integer upid;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    @ApiModelProperty(value = "数据集路径")
    private String dataPath;
    @ApiModelProperty(value = "导入视频路径")
    private String expVideoPath;
    @ApiModelProperty(value = "案例文档路径")
    private String exampleDoc;

    @ApiModelProperty(value = "文件数量")
    private Integer flag;

    @ApiModelProperty(value = "班级id")
    private Integer gradeid;
    @ApiModelProperty(value = "班级名称")
    private String gname;

}
