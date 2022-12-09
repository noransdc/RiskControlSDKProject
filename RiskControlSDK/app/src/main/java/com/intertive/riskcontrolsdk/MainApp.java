package com.intertive.riskcontrolsdk;

import android.app.Application;

import com.risk.riskcontrol.RiskControl;

public class MainApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();



        RiskControl.init(this);
        RiskControl.initApp(this);


    }


}
