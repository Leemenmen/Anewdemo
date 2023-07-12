package com.newdmsp.demo.entity;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel(value = "问答实体")
public class Qtoa {
    @ApiModelProperty(value = "问答id")
    private Integer id;
    @ApiModelProperty(value = "学生id")
    private Integer sid;
    @ApiModelProperty(value = "问题")
    private String question;
    @ApiModelProperty(value = "回答")
    private String answer;
    @ApiModelProperty(value = "提问时间")
    private String queTime;
    @ApiModelProperty(value = "回答时间")
    private String ansTime;

    @ApiModelProperty(value = "班级id")
    private Integer gradeid;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "真实名称")
    private String realname;
}
