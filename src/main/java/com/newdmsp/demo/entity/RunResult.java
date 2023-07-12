package com.newdmsp.demo.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RunResult {

    private Integer csvCount;
    private Integer figCount;
    private String figName;
    private String codeName;
    private String tag;
}
