package com.newdmsp.demo.entity;

import lombok.Data;
import lombok.ToString;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ToString
@Data
/**
 * 学生报告得分，总分为
（3/5+4/5+5/10+6/10+7/10+8/30+9/30)/7
大于等于0.7代表考核通过(Score)实体类
 *
 * @author makejava
 * @since 2022-11-15 09:47:22
 */
@ApiModel(value="学生报告得分，总分为（3/5+4/5+5/10+6/10+7/10+8/30+9/30)/7大于等于0.7代表考核通过")
public class Score {
       
    private Integer id;
    /**
     * 实验报告id
     */
   @ApiModelProperty(value = "实验报告id")   
    private Integer recordId;
    /**
     * 场景导入打分
     */
   @ApiModelProperty(value = "场景导入打分")   
    private Double videoDescScore;
    /**
     * 数据集描述打分
     */
   @ApiModelProperty(value = "数据集描述打分")   
    private Double dataDescScore;
    /**
     * 实验过程打分
     */
   @ApiModelProperty(value = "实验过程打分")   
    private Double expProcessScore;
    /**
     * 实验结果打分
     */
   @ApiModelProperty(value = "实验结果打分")   
    private Double expResultScore;
    /**
     * 实验体会打分
     */
   @ApiModelProperty(value = "实验体会打分")   
    private Double experienceScore;
    /**
     * 实验代码打分
     */
   @ApiModelProperty(value = "实验代码打分")   
    private Double recordCodeScore;
    /**
     * 总体描述打分
     */
   @ApiModelProperty(value = "总体描述打分")   
    private Double totalDescScore;
    /**
     * 创建时间
     */
   @ApiModelProperty(value = "创建时间")   
    private String createTime;
    /**
     * 打分时间
     */
   @ApiModelProperty(value = "打分时间")   
    private String lastTime;


}

