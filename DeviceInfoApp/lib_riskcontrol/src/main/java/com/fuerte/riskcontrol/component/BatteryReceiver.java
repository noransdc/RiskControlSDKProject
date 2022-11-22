package com.fuerte.riskcontrol.component;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.fuerte.riskcontrol.util.TimeUtil;

import java.util.Locale;

public class BatteryReceiver extends BroadcastReceiver {

    private long lastTime = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;

        long current = TimeUtil.getMilliTimestamp();
        if (current - lastTime < 15000){
            return;
        }
        lastTime = current;

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
        int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);

        try {
            Float batteryTotal = Float.valueOf(intent.getExtras().getInt("scale"));
            Float level = Float.valueOf(intent.getExtras().getInt("level"));
            if (level != null && batteryTotal != null) {
                String percent = String.format(Locale.getDefault(), "%.2f", level / batteryTotal);
                SpConstant.setBatteryLevel(context, percent);
            }

            SpConstant.setBatteryMax(context, String.valueOf(batteryTotal));
            SpConstant.setBatteryNow(context, String.valueOf(level));

        } catch (Exception e) {
            e.printStackTrace();
        }

        SpConstant.setPlugged(context, plugged);
        SpConstant.setHealth(context, health);
        SpConstant.setStatus(context, status);
        SpConstant.setTemperature(context, String.valueOf(temperature));
        SpConstant.setTechnology(context, technology);

    }

}
