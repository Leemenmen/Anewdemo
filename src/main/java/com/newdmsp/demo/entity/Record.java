package com.newdmsp.demo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.Value;

@Data
@ToString
@ApiModel(value = "代码记录实体类")
public class Record {
    @ApiModelProperty(value = "记录id")
    private Integer id;
    @ApiModelProperty(value = "实验id")
    private Integer expid;
    @ApiModelProperty(value = "学生id")
    private Integer sid;
    @ApiModelProperty(value = "班级id")
    private Integer gid;
    @ApiModelProperty(value = "实验结果")
    private String expresult;
    @ApiModelProperty(value = "实验过程")
    private String expprocess;
    @ApiModelProperty(value = "实验报告路径")
    private String recordurl;
    @ApiModelProperty(value = "实验代码路径")
    private String recordcode;
    @ApiModelProperty(value = "实验分数")
    private Float score;
    @ApiModelProperty(value = "上传时间")
    private String createTime;
    @ApiModelProperty(value = "修改次数")
    private Integer count;
    @ApiModelProperty(value = "0保存，1提交")
    private Integer flag;
    @ApiModelProperty(value = "最后一次提交时间")
    private String lastTime;
    @ApiModelProperty(value = "结果附件为png的数量")
    private Integer png;
    @ApiModelProperty(value = "结果附件为csv的数量")
    private Integer csv;
    @ApiModelProperty(value = "心得与体会")
    private String experience;
    @ApiModelProperty(value = "视频描述")
    private String videoDesc;
    @ApiModelProperty(value = "是1否0包含数据集")
    private Integer dataFlag;
    @ApiModelProperty(value = "数据集描述")
    private String dataDesc;
    @ApiModelProperty(value = "综合体会")
    private String totalDesc;


    @ApiModelProperty(value = "实验名称", required = true)
    private String expname;
    @ApiModelProperty(value = "实验任务,默认值无意义", example = "0")
    private String expwork;
    @ApiModelProperty(value = "实验目标,默认值无意义", example = "0")
    private String exptarget;
    @ApiModelProperty(value = "代码路径,默认值无意义", example = "0")
    private String expcodeurl;
    @ApiModelProperty(value = "用户id,默认值无意义", example = "0")
    private Integer uid;
    @ApiModelProperty(value = "用户名,默认值无意义", example = "0")
    private String username;
    @ApiModelProperty(value = "真实名称,默认值无意义", example = "0")
    private String realname;
    @ApiModelProperty(value = "flag标识实验有无数据集", example = "0")
    private Integer eflag;
    @ApiModelProperty(value = "场景视频路径,默认值无意义", example = "0")
    private String expVideoPath;
    @ApiModelProperty(value = "案例文件路径", example = "0")
    private String exampleDoc;
}
