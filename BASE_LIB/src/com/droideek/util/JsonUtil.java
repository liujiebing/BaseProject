package com.droideek.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Droideek on 2016/6/28.
 */
public class JsonUtil {

    // 把JSON文本parse为JSONObject或者JSONArray
//    public static final Object parse(String text){
//
//        return null;
//    }


    public static JSONObject parseObject(Object o) {
        if (null == o) return null;
        return parseObject(o.toString());
    }

    /**
     * // 把JSON文本parse成JSONObject
     *
     * @param text
     * @return
     */
    public static JSONObject parseObject(String text) {
        if (TextUtils.isEmpty(text)) return null;

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(text);
        } catch (Exception e) {
            //
        }

        return jsonObj;
    }

    /**
     * 把JSON文本parse为JavaBean
     *
     * @return
     */
    public static final <T> T parseObject(String json, Class<T> clazz) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        T t=null;
//        try {
//             t= objectMapper.readValue(text, clazz);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return t;

        Gson g = new Gson();
        T temp = null;

        if (!TextUtils.isEmpty(json)) {
            try {
                temp = g.fromJson(json, clazz);
            } catch (Exception e) {

            }

        }
        return temp;
    }


    /**
     * // 把JSON文本parse成JSONArray
     * @param text
     * @return
     */
//    public static final JSONArray parseArray(String text){
//        return null;
//    }

    /**
     * //把JSON文本parse成JavaBean集合
     * @param text
     * @param <T>
     * @return
     */
    public static final <T> List<T> parseArray(String text){
//        //ObjectMapper objectMapper = new ObjectMapper();
////        List<T> t=null;
////        try {
////            t= objectMapper.readValue(text, new ArrayList<T>().getClass());
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        return t;
//
//        return null;
        Gson gson = new Gson();
        List<T> list = new ArrayList<>();
        try {
            list = gson.fromJson(text, new TypeToken<List<T>>(){}.getType());
        } catch (Exception e) {
            //log
        }

        return list;
   }

    /**
     * // 将JavaBean序列化为JSON文本
     *
     * @param object
     * @return
     */
    public static final String toJSONString(Object object) {
        Gson gson = new Gson();

        try {
            return gson.toJson(object);
        } catch (Exception e) {
            //
        }
        return "";
    }



    /**
     * // 将JavaBean序列化为带格式的JSON文本
     * @param object
     * @param prettyFormat
     * @return
     */
//    public static final String toJSONString(Object object, boolean prettyFormat){
//        return null;
//    }

    /**
     *  将JavaBean转换为JSONObject或者JSONArray。
     * @param javaObject
     * @return
     */
//    public static final Object toJSON(Object javaObject){
//        return null;
//    }

}


