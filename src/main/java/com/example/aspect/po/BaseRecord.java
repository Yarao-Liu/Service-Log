package com.example.aspect.po;

import com.example.aspect.aspect.MethodType;
import lombok.Data;

import java.util.Date;

/**
 * @author Mr.Liu
 */
@Data
public class BaseRecord {

    private String id;

    private String methodName;

    private MethodType type;

    private String details;

    private String describe;
    /**
     * 默认为当前时间,可以重新格式化覆盖掉
     */
    private Date date=new Date();

    @Override
    public String toString() {
        return "BaseRecord{" +
                "id='" + id + '\'' +
                ", methodName='" + methodName + '\'' +
                ", type=" + type +
                ", details='" + details + '\'' +
                ", describe='" + describe + '\'' +
                ", date=" + date +
                '}';
    }
}
