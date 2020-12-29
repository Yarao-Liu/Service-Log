package com.example.aspect.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.example.aspect.config.Constant.UNKNOWN;
import static com.example.aspect.config.Constant.X_FORWARDED_FOR;

/**
 * @author Mr.Liu
 */
@Service
public class TestService {
    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"};

    /***
     * 获取客户端ip地址(可以穿透代理)
     * @param request request
     * @return ip地址字符串
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !UNKNOWN.equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
    public static String getRemortIP(HttpServletRequest request) {
        if (request.getHeader(X_FORWARDED_FOR) == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader(X_FORWARDED_FOR);
    }
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader(X_FORWARDED_FOR);
        if(ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
