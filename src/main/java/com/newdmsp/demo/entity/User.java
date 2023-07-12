package com.newdmsp.demo.entity;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.Value;

import java.util.List;

@Data
@ToString
@ApiModel(value = "用户实体类")
public class User {
    @ApiModelProperty(value = "用户id")
    private Integer id;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "角色id")
    private Integer roleid;
    @ApiModelProperty(value = "真实名称")
    private String realname;
    @ApiModelProperty(value = "班级id")
    private Integer gradeid;
    @ApiModelProperty(value = "实验列表")
    private List<Exp> exps;
    @ApiModelProperty(value = "0账号可用,1不可用")
    private Integer enable;

    @ApiModelProperty(value = "新增实验id")
    private Integer newExpId;

    private Integer gid;
    @ApiModelProperty(value = "班级名称")
    private String gname;
//    private String opassword;
}
