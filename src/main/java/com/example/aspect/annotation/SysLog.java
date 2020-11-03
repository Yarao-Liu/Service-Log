package com.example.aspect.annotation;


import com.example.aspect.aspect.MethodType;

import java.lang.annotation.*;

/**
 * @author Mr.Liu
 * @description 日志审计注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    /**
     * 方法名称
     */
    String METHOD() default "UNKNOWN NAME";
    /**
     * 方法类型
     */
    MethodType TYPE() default MethodType.UNKNOWN_TYPE;
    /**
     * 方法细节
     */
    String DETAILS() default " UNKNOWN DETAILS !";
    /**
     * 方法描述
     */
    String DESCRIBE() default " UNKNOWN DESCRIBE !";
}
