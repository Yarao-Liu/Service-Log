package com.example.aspect.controller;

import com.example.aspect.aspect.MethodType;
import com.example.aspect.annotation.SysLog;
import com.example.aspect.po.User;
import com.example.aspect.service.TestService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mr.Liu
 */
@RestController
@SysLog(METHOD = "test",DESCRIBE = "TestController")
public class TestController {

    @ResponseBody
    @RequestMapping(value = "sayHello",method = RequestMethod.GET)
    @SysLog(METHOD = "sayHello",TYPE = MethodType.TEST,DETAILS = "",DESCRIBE = "")
    public String sayHello(String name){
        return "hello world "+name+" !";
    }

    @ResponseBody
    @RequestMapping(value = "double",method = RequestMethod.GET)
    @SysLog(METHOD = "double",TYPE = MethodType.TEST,DETAILS = "",DESCRIBE = "")
    public String doubleParam(String username,String password){
        return "hello world "+username+":"+password+"!";
    }

    @ResponseBody
    @RequestMapping(value = "login",method = RequestMethod.GET)
    @SysLog(METHOD = "login",TYPE = MethodType.FIND,DETAILS = "登录",DESCRIBE = "对输入的账号和密码进行比对!")
    public String login(User user){
        return "hello world "+user+" !";
    }

    @ResponseBody
    @SysLog
    @RequestMapping(value = "getIP",method = RequestMethod.GET)
    public String getIP(HttpServletRequest request){

        String clientIpAddress = TestService.getClientIpAddress(request);
        String remortIP = TestService.getRemortIP(request);
        String IpAddr = TestService.getIpAddr(request);
        return clientIpAddress+","+remortIP+","+IpAddr;
    }

}
