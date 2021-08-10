package com.zeffon.danzhu.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeffon.danzhu.exception.http.ServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Create by Zeffon on 2020/10/1
 */
@Component
public class GenericAndJson {

    // 依赖注入就是对实例对象的成员变量进行注入，它需要有个实例化的过程
    private static ObjectMapper mapper;

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        GenericAndJson.mapper = mapper;
    }

    /**
     * 对象转化成json字符串
     * @param o 传入的对象
     * @param <T> 泛型处理
     * @return String
     */
    public static <T> String objectToJson(T o) {
        try {
            return GenericAndJson.mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }

    /**
     * json转变成单一对象
     * @param s json字符
     * @param tr
     * @param <T>
     * @return 单一对象
     */
    public static <T> T jsonToObject(String s, TypeReference<T> tr) {
        if (s == null) {
            return null;
        }
        try {
            T o = GenericAndJson.mapper.readValue(s, tr);
            return o;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }

    /**
     * json字符串转变数组对象(将List<T>中的`T` 视为泛型的)
     * 不可用，并非强类型转换，只是转换成HashMap类型，这也达到转成json的效果
     * 但是这不是理想的结果，我需要的是强类型转换(原因：强类型转换可以利用对应的实体类类，实现特定的方法)
     * @param s json字符串
     * @param <T>
     * @return 数组对象
     */
//    public static <T> List<T> jsonToList(String s) {
//        if (s == null) {
//            return null;
//        }
//        try {
//            List<T> list = GenericAndJson.mapper.readValue(s, new TypeReference<List<T>>() {
//            });
//            return list;
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new ServerErrorException(9999);
//        }
//    }

    /**
     * json字符串转变数组对象(将List<T>整体视为泛型的)
     * @param s json字符
     * @param tr TypeReference<T>
     * @param <T>
     * @return 数组对象
     */
//    public static <T> T jsonToList(String s, TypeReference<T> tr) {
//        if (s == null) {
//            return null;
//        }
//        try {
//            T list = GenericAndJson.mapper.readValue(s, tr);
//            return list;
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new ServerErrorException(9999);
//        }
//    }
}
