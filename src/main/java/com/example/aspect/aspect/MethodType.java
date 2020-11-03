package com.example.aspect.aspect;

/**
 * @author Mr.Liu
 * @Description 控制器方法的类型
 */
public enum MethodType {
    /**测试*/
    TEST,
    /**未知*/
    UNKNOWN_TYPE,
    /**查询*/
    FIND,
    /**新建*/
    BUILD,
    /**删除*/
    DELETE,
    /**更改*/
    UPDATE,
    /**插入*/
    INSERT,
    /**下载*/
    DOWNLOAD,
    /**上传*/
    UPLOAD
}
