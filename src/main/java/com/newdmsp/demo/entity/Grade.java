package com.newdmsp.demo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel(value = "班级实体类")
public class Grade {

    @ApiModelProperty(value = "班级id")
    private Integer id;

    @ApiModelProperty(value = "班级名称")
    private String gname;

    @ApiModelProperty(value = "教师id")
    private Integer tid;
}
