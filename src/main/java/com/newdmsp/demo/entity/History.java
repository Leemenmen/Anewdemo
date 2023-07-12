package com.newdmsp.demo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.ToString;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.util.Date;

@Data
@ToString
@ApiModel(value = "历史代码实体类")
public class History {
    @ApiModelProperty(value = "历史代码id")
    private Integer id;
    @ApiModelProperty(value = "学生id")
    private Integer sid;
    @ApiModelProperty(value = "实验id")
    private Integer expId;
    @ApiModelProperty(value = "学生姓名")
    private String sname;
    @ApiModelProperty(value = "代码保存路径")
    private String codeurl;
    @ApiModelProperty(value = "代码结果")
    private String resulturl;
    @ApiModelProperty(value = "插入数据库时间")
    private String createtime;
    @ApiModelProperty(value = "图片数量")
    private Integer figCount;
    @ApiModelProperty(value = "图片数量")
    private Integer csvCount;


    @ApiModelProperty(value = "实验名称")
    private String expname;
    //数据库的三种类都继承了java.util.Date，在除了数据库的情况下使用
//    java.sql.Date sDate = new java.sql.Date(createtime.getTime());//年月日
//    java.sql.Time sTime = new java.sql.Time(sDate.getTime());//时分秒
//    java.sql.Timestamp sTimeStamp = new java.sql.Timestamp(createtime.getTime());//年月日时分秒毫秒


}
