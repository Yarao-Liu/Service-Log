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

    private Date date=new Date();
}
