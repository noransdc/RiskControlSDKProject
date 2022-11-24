package com.risk.riskcontrol.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Nevio
 * on 2022/11/23
 */
public class JsonSimpleUtil {

    private final static String[] basicTypeArr = {"java.lang.Integer",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.Byte",
            "java.lang.Boolean",
            "java.lang.Character",
            "java.lang.String",
            "int", "double", "long", "short", "byte", "boolean", "char", "float"};

    private static boolean isBasicType(String type) {
        for (String s : basicTypeArr) {
            if (s.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public static JSONObject objToJsonObj(Object obj) {
        JSONObject jsonObject = new JSONObject();
        if (obj == null) {
            return jsonObject;
        }
        try {
            Class clz = obj.getClass();
            Field[] fieldArr = clz.getDeclaredFields();
            for (Field field : fieldArr) {
                if (field == null) {
                    continue;
                }
                field.setAccessible(true);
                String type = field.getType().getName();
                if (isBasicType(type)) {
                    jsonObject.put(field.getName(), field.get(obj));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static <T> JSONArray listToJsonArray(List<T> list) {
        JSONArray jsonArray = new JSONArray();
        if (list == null || list.isEmpty()) {
            return jsonArray;
        }

        for (Object o : list) {
            jsonArray.put(objToJsonObj(o));
        }
        return jsonArray;
    }

    public static String objToJsonStr(Object obj) {
        return objToJsonObj(obj).toString();
    }

    public static <T> String listToJsonStr(List<T> list){
        return listToJsonArray(list).toString();
    }

}
