package com.risk.riskcontrol.util;

import android.app.Application;
import android.content.Context;

/**
 * @author Nevio
 * on 2022/2/4
 */
public class ContextUtil {


    private static Context mApplication;


    public static void init(Context app){
        mApplication = app;
    }

    public static Context getAppContext(){
        return mApplication;
    }

}
