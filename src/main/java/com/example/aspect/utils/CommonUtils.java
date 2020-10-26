package com.example.aspect.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.Liu
 */
public class CommonUtils {

    public static HashMap<String,Object> Array2Map(String[]parameters,Object[]values){
        HashMap<String, Object> hashMap = new HashMap<>();
        for (int i =0;i<parameters.length;i++){
                hashMap.put(parameters[i],values[i]);
            }
        return hashMap;
    }
}
