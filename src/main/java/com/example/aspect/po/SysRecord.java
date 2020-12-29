package com.example.aspect.po;

import lombok.Data;

/**
 *  业务相关的属性
 * @author Mr.Liu
 */
@Data
public class SysRecord extends BaseRecord {
    /**
     * 操作时间
     */
    private String operationTime;
    /**
     * 操作者
     */
    private String operationUser;

    @Override
    public String toString() {
        return "SysRecord{" +super.toString()+
                "operationTime='" + operationTime + '\'' +
                ", operationUser='" + operationUser + '\'' +
                '}';
    }
}
