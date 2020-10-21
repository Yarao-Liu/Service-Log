package com.example.aspect.aspect;

import cn.hutool.core.bean.BeanUtil;
import com.example.aspect.annotation.SysLog;
import com.example.aspect.po.User;
import com.example.aspect.utils.EntityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

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
        String packageName = "com.example.aspect.po";

        ArrayList<Object> reBuildClass = EntityUtils.reBuildClass(args,packageName);
        System.out.println(parameterNames[0]);
        if (reBuildClass.size()>0){
            System.out.println(reBuildClass);
        }else {
            System.out.println(args[0]);
        }
    }
    @After("logAspect()")
    public void doAfter(JoinPoint joinPoint) {
        SysLog sysLog = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(SysLog.class);
        //未通过注解设置method_name
        if ("UNKNOWN".equals(sysLog.METHOD())) {
            String method = sysLog.METHOD();
            log.error("8: annotation: " + method);
            String name = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
            log.error("9: aop: " + name);
        }
        log.error("10: " + sysLog.METHOD());
        log.error("11: " + sysLog.DESCRIBE());
        log.error("12: " + sysLog.TYPE());
    }

    @AfterReturning("logAspect()")
    public void doAfterReturning() {
    }

    @AfterThrowing("logAspect()")
    public void doAfterThrowing() {
    }

    @Around("logAspect()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        Object proceed = point.proceed();
        return proceed;
    }
}
