package com.example.aspect.annotation;


import java.lang.annotation.*;

/**
 * @author Mr.Liu
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    String METHOD() default "UNKNOWN";

    MethodType TYPE() default MethodType.UNKNOWN_TYPE;

    String DETAILS() default " UNKNOWN DETAILS !";

    String DESCRIBE() default " UNKNOWN DESCRIBE !";
}
