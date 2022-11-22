package com.fuerte.riskcontrol.util;

import android.app.Application;
import android.content.Context;

/**
 * @author Nevio
 * on 2022/2/4
 */
public class ContextUtil {


    private static Application mApplication;


    public static void init(Application app){
        mApplication = app;
    }

    public static Application getApplication(){
        return mApplication;
    }

    public static Context getAppContext(){
        return mApplication.getApplicationContext();
    }

}
