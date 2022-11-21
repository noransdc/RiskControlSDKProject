package com.fuerte.deviceinfoapp;

import android.app.Application;

import com.device.deviceinfosdk.DeviceInfoSDK;

public class MainApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();


        DeviceInfoSDK.init(this);


    }


}
