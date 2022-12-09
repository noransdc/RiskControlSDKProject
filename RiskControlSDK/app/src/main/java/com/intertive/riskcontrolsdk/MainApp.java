package com.intertive.riskcontrolsdk;

import android.app.Application;

import com.risk.riskcontrol.RiskControlSDK;

public class MainApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();



        RiskControlSDK.init(this);
        RiskControlSDK.initApp(this);


    }


}
