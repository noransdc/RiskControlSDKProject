package com.fuerte.riskcontrol.component;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.fuerte.riskcontrol.util.CollectionUtil;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nevio
 * on 2022/3/2
 */
public class AppLifeManager {


    public static AppLifeManager instance;
    private final List<WeakReference<Activity>> weakActivityList = new ArrayList<>();
    private int count = 0;
    private boolean isForeground = false;
    private final List<OnForegroundListener> listenerList = new ArrayList<>();


    private AppLifeManager(){}


    public static AppLifeManager getInstance(){
        if (instance == null){
            synchronized (AppLifeManager.class){
                if (instance == null){
                    instance = new AppLifeManager();
                }
            }
        }
        return instance;
    }

    public void register(Application application){
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull @NotNull Activity activity, @Nullable Bundle savedInstanceState) {
                if (weakActivityList != null){
                    weakActivityList.add(new WeakReference<>(activity));
                }
//                LanguageUtil.onActivityLifeCallback(activity);
                registerBatteryReceiver(activity);
            }

            @Override
            public void onActivityStarted(@NonNull @NotNull Activity activity) {
                count++;
                if (count == 1){
                    isForeground = true;
                    notifyForeground(true);
                }
            }

            @Override
            public void onActivityResumed(@NonNull @NotNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull @NotNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull @NotNull Activity activity) {
                count--;
                if (count == 0){
                    isForeground = false;
                    notifyForeground(false);
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull @NotNull Activity activity, @NonNull @NotNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull @NotNull Activity activity) {
                if (CollectionUtil.isNullOrEmpty(weakActivityList)){
                    return;
                }
                for (int i = weakActivityList.size() - 1; i >= 0; i--) {
                    WeakReference<Activity> reference = weakActivityList.get(i);
                    if (reference != null && reference.get() == activity){
                        weakActivityList.remove(reference);
                    }
                }
                unregisterBatteryReceiver(activity);
            }
        });
    }

    public boolean isForeground(){
        return isForeground;
    }

    public void registerForegroundListener(OnForegroundListener listener){
        if (listener == null || listenerList == null){
            return;
        }
        listenerList.add(listener);
    }

    public void unregisterForegroundListener(OnForegroundListener listener){
        if (listener == null || listenerList == null){
            return;
        }
        listenerList.remove(listener);
    }

    public void notifyForeground(boolean isForeground){
        if (listenerList == null){
            return;
        }
        for (OnForegroundListener listener : listenerList) {
            listener.notifyForeground(isForeground);
        }
    }

    public interface OnForegroundListener{
        void notifyForeground(boolean isForeground);
    }

    public void finishAll(){
        if (CollectionUtil.isNullOrEmpty(weakActivityList)){
            return;
        }
        for (WeakReference<Activity> reference : weakActivityList) {
            if (reference.get() != null){
                Activity activity = (Activity) reference.get();
                activity.finish();
            }
        }
    }

    public void finishAllButMainActivity(){
        if (CollectionUtil.isNullOrEmpty(weakActivityList)){
            return;
        }
        for (WeakReference<Activity> reference : weakActivityList) {
            if (reference.get() != null){
                Activity activity = (Activity) reference.get();
                if (activity.getComponentName() != null){
                    String clsName = activity.getComponentName().getClassName();

                    if (!TextUtils.isEmpty(clsName) && !clsName.contains("MainActivity")){
                        activity.finish();
                    }
                }
            }
        }
    }

    public void finishAllButMainAndLoginActivity(){
        if (CollectionUtil.isNullOrEmpty(weakActivityList)){
            return;
        }
        for (WeakReference<Activity> reference : weakActivityList) {
            if (reference.get() != null){
                Activity activity = (Activity) reference.get();
                if (activity.getComponentName() != null){
                    String clsName = activity.getComponentName().getClassName();

                    if (!TextUtils.isEmpty(clsName) && !clsName.contains("MainActivity") && !clsName.contains("LoginActivity")){
                        activity.finish();
                    }
                }
            }
        }
    }

    @Nullable
    public Activity getTaskTopActivity(){
        if (CollectionUtil.isNullOrEmpty(weakActivityList)){
            return null;
        }
        WeakReference<Activity> reference = weakActivityList.get(weakActivityList.size() - 1);
        if (reference == null){
            return null;
        }
        return reference.get();
    }

    private BatteryReceiver batteryReceiver;

    private void registerBatteryReceiver(Activity activity) {
        if (activity == null){
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        batteryReceiver = new BatteryReceiver();
        activity.registerReceiver(batteryReceiver, intentFilter);
    }

    private void unregisterBatteryReceiver(Activity activity) {
        if (activity == null){
            return;
        }
        if (batteryReceiver != null) {
            activity.unregisterReceiver(batteryReceiver);
        }
    }


}
