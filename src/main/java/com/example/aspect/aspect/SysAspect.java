package com.example.aspect.aspect;

import com.example.aspect.annotation.SysLog;
import com.example.aspect.utils.EntityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    public void logAspect() {
    }

    @Before("logAspect()")
    public void doBefore(JoinPoint joinPoint) {
        String packageName = "com.example.aspect.po";
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        //可以组装 describe 和 details
        for (int i = 0; i < args.length; i++) {
            log.error("1: " + parameterNames[i]);
            log.error("2: " + args[i]);
            log.error("3: " + args[i].getClass());
            //param的参数类型
            Class aClass = EntityUtils.assertClass(args[i], packageName);
            if (aClass != null) {

            }
        }
    }

    public Set getPackageAllClasses(String packageName) {
        Set<Class<?>> classes;
        try {
            classes = EntityUtils.getClasses(packageName);
        } catch (IOException ignored) {
            return null;
        }
        return classes;
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
