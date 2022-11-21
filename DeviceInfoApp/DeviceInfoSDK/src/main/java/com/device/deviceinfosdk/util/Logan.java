package com.device.deviceinfosdk.util;

import android.util.Log;

import androidx.annotation.NonNull;


/**
 * @author Nevio
 * on 2019/2/21
 */
public class Logan {

    // 输出日志类型，v:输出所有信息
    private static char LOG_TYPE = 'v';
    // sdcard中的文件夹名
    private static String mainProjectName = "Hiper";
    private static boolean isLogOpen = false;


    public static void v(Object obj) {
        log("--x--" + getCurrentClassName() + "--:" + obj, 'v');
    }

    public static void v(String title, Object obj) {
        log("--x--" + getCurrentClassName() + "--:" + title + ":" + obj, 'v');
    }

    public static void d(Object obj) {
        log("--x--" + getCurrentClassName() + "--:" + obj, 'd');
    }

    public static void d(String title, Object obj) {
        log("--x--" + getCurrentClassName() + "--:" + title + ":" + obj, 'd');
    }

    public static void i(Object obj) {
        log("--x--" + getCurrentClassName() + "--:" + obj, 'i');
    }

    public static void i(String title, Object obj) {
        log("--x--" + getCurrentClassName() + "--:" + title + ":" + obj, 'i');
    }

    public static void w(Object obj) {
        log("--x--" + getCurrentClassName() + "--:" + obj, 'w');
    }

    public static void w(String title, Object obj) {
        log("--x--" + getCurrentClassName() + "--:" + title + ":" + obj, 'w');
    }

    public static void e(Object obj) {
        log("--x--" + getCurrentClassName() + "--:" + obj, 'e');
    }

    public static void e(String title, Object obj) {
        log("--x--" + getCurrentClassName() + "--:" + title + ":" + obj, 'e');
    }


    public static void init(boolean logOpen) {
        isLogOpen = logOpen;
    }


    /**
     * 根据tag, msg和等级，输出日志
     */
    private static void log(String logStr, char level) {
        if (!isLogOpen) {
            return;
        }
        if ('e' == level && ('e' == LOG_TYPE || 'v' == LOG_TYPE)) {
            Log.e(mainProjectName, logStr);
        } else if ('w' == level && ('w' == LOG_TYPE || 'v' == LOG_TYPE)) {
            Log.w(mainProjectName, logStr);
        } else if ('i' == level && ('i' == LOG_TYPE || 'v' == LOG_TYPE)) {
            Log.i(mainProjectName, logStr);
        } else if ('d' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
            Log.d(mainProjectName, logStr);
        } else {
            Log.v(mainProjectName, logStr);
        }

    }


    @NonNull
    private static String getCurrentClassName() {
        int level = 2;
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String className = stacks[level].getClassName();
        return className.substring(className.lastIndexOf(".") + 1);
    }



}
