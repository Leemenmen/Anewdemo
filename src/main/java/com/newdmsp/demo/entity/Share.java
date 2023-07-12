package com.newdmsp.demo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;


@Data
@ToString
@ApiModel(value = "共享实体类")
public class Share {
    @ApiModelProperty(value = "共享数据id")
    private Integer id;
    @ApiModelProperty(value = "共享者id")
    private Integer userId;
    @ApiModelProperty(value = "共享描述")
    private String uploadDes;
    @ApiModelProperty(value = "下载次数")
    private Integer downloadCount;
    @ApiModelProperty(value = "教师打分")
    private Float teachScore;
    @ApiModelProperty(value = "标志：1表示通过;0表示未通过;3表示未审批")
    private Integer tid;
    @ApiModelProperty(value = "存储路径")
    private String storagePath;
    @ApiModelProperty(value = "审批结果说明")
    private String flagDes;
    @ApiModelProperty(value = "标识：0代码，1数据")
    private Integer flag;
    @ApiModelProperty(value = "上传时间")
    private String createTime;
    @ApiModelProperty(value = "文件评价")
    private String scoreDes;

    private MultipartFile uploadfile;
    private Integer gradeid;
    private String gname;
    private String username;
    private String realname;


}
