package com.fuerte.deviceinfoapp;

import android.app.Application;

import com.fuerte.riskcontrol.RiskControlSDK;

public class MainApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();



        RiskControlSDK.init(this);
        RiskControlSDK.initApp(this);


    }


}
