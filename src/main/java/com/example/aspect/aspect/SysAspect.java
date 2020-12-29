package com.example.aspect.aspect;

import com.example.aspect.utils.ReflectionUtils;
import com.example.aspect.annotation.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Mr.Liu
 */
@Slf4j
@Aspect
@Component
@Order(1)
public class SysAspect {

    @Pointcut(value = "@annotation(com.example.aspect.annotation.SysLog)")
    public void logAspect() { }

    @Before("logAspect()")
    public void doBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature =(MethodSignature)joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        //entity所在包的位置
        String packageName = "com.example.aspect.po";

        ArrayList<Object> reBuildClass = ReflectionUtils.reBuildClass(args,packageName);
        //参数列表
        log.warn("doBefore:"+ Arrays.toString(args));
        //变量列表
        log.warn("doBefore: "+ Arrays.toString(parameterNames));

        if (reBuildClass.size()>0){
            log.warn("doBefore: "+reBuildClass.toString());
        }else if (ReflectionUtils.isPrimitive(args[0])){
            //字符串或基本数据类型及其封装类//理论上强制类型转换即可
            ArrayList<String> paramString = ReflectionUtils.param2String(args);
            System.out.println(paramString);
        }else {
            log.error("UNKNOWN DATA TYPE......");
        }
    }
    @After("logAspect()")
    public void doAfter(JoinPoint joinPoint) {
        SysLog sysLog = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(SysLog.class);
        //未通过注解设置method_name
        if ("UNKNOWN".equals(sysLog.METHOD())) {
            String method = sysLog.METHOD();
            log.warn("doAfter: " + method);
            String name = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
            log.warn("doAfter: " + name);
        }
        log.warn("doAfter: " + sysLog.METHOD());
        log.warn("doAfter: " + sysLog.DESCRIBE());
        log.warn("doAfter: " + sysLog.TYPE());
    }

    @AfterReturning("logAspect()")
    public void doAfterReturning() {
        //TODO
    }

    @AfterThrowing("logAspect()")
    public void doAfterThrowing() {
        //TODO
    }

    @Around("logAspect()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        Object proceed = point.proceed();
        return proceed;
    }
}
