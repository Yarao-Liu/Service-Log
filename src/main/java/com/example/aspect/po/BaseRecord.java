package com.example.aspect.po;

import lombok.Data;

import java.util.Date;

/**
 * @author Mr.Liu
 */
@Data
public class BaseRecord {

    private String id;

    private String methodName;

    private String type;

    private String details;

    private String describe;
    /**
     * 默认为当前时间,可以重新格式化覆盖掉
     */
    private Date date=new Date();
}
