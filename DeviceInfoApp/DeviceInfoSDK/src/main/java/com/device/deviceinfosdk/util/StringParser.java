package com.device.deviceinfosdk.util;

/**
 * @author Nevio
 * on 2022/3/16
 */
public class StringParser {


    public static int toInt(String str){
        try {
               return Integer.parseInt(str);
        } catch (Exception e) {

        }
        return 0;
    }

    public static long toLong(String str){
        try {
            return Long.parseLong(str);
        } catch (Exception e) {

        }
        return 0;
    }

    public static double toDouble(String str){
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {

        }
        return 0;
    }

    public static float toFloat(String str){
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {

        }
        return 0;
    }
}
