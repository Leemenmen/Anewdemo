package com.newdmsp.demo.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel(value = "接口返回到前端的值",description = "状态码+数据")
public class Result<T> {

    @ApiModelProperty(value = "状态码", dataType = "int")
    private int code;   //状态码
    @ApiModelProperty(value = "提示信息", dataType = "String")
    private String msg; //提示信息
    @ApiModelProperty(value = "单条数据", dataType = "T")
    private T data; //数据
    @ApiModelProperty(value = "多条数据", dataType = "List<T>")
    private List<T> datas;  //多条数据
    @ApiModelProperty(value = "分页时显示数据总条目", dataType = "int")
    private int total;  //分页获取数据时，存储符合程序条件的数据总量


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}

