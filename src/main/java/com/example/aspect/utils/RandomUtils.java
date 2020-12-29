package com.example.aspect.utils;

import java.util.UUID;

/**
 * @author Mr.Liu
 * 随机生成工具
 */
public class RandomUtils {

    public static String randomString8(){
        String substring = UUID.randomUUID().toString().substring(0, 8);
        return  substring;
    }

    public static void main(String[] args) {
        String randomString8 = randomString8();
        System.out.println(randomString8);
    }

}
