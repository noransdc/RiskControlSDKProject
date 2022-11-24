package com.risk.riskcontrol.component;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;


/**
 * @author Conrad
 * on 2018/12/13.
 */
public class SpConstant {

    private static final String SP_FILE_NAME = "SpCons";
    public static final String SP_USER_ID= "sp_user_id";
    public static final String SP_TOKEN = "sp_token";
    public static final String SP_BACK_LOC = "sp_back_loc";//可拖动moveBack坐标
    public static final String SP_ACCOUNT = "sp_account";//用户名/手机账号
    public static final String SP_PASSWORD = "sp_password";//密码


    public static void setBatteryLevel(Context context, String deviceId){
        if (context == null){
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("setBatteryLevel", deviceId);
        editor.apply();
    }

    @NonNull
    public static String getBatteryLevel(Context context){
        if (context == null){
            return "";
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        return sp.getString("setBatteryLevel", "");
    }


    public static void setBatteryMax(Context context, String city){
        if (context == null){
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("setBatteryMax", city);
        editor.apply();
    }

    @NonNull
    public static String getBatteryMax(Context context){
        if (context == null){
            return "";
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        return sp.getString("setBatteryMax", "");
    }


    public static void setBatteryNow(Context context, String city){
        if (context == null){
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("setBatteryNow", city);
        editor.apply();
    }

    @NonNull
    public static String getBatteryNow(Context context){
        if (context == null){
            return "";
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        return sp.getString("setBatteryNow", "");
    }


    public static void setPlugged(Context context, int city){
        if (context == null){
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("setPlugged", city);
        editor.apply();
    }

    @NonNull
    public static int getPlugged(Context context){
        if (context == null){
            return 0;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        return sp.getInt("setPlugged", 0);
    }


    public static void setHealth(Context context, int city){
        if (context == null){
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("setHealth", city);
        editor.apply();
    }

    @NonNull
    public static int getHealth(Context context){
        if (context == null){
            return 0;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        return sp.getInt("setHealth", 0);
    }


    public static void setStatus(Context context, int city){
        if (context == null){
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("setStatus", city);
        editor.apply();
    }

    @NonNull
    public static int getStatus(Context context){
        if (context == null){
            return 0;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        return sp.getInt("setStatus", 0);
    }


    public static void setTemperature(Context context, String deviceId){
        if (context == null){
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("setTemperature", deviceId);
        editor.apply();
    }

    @NonNull
    public static String getTemperature(Context context){
        if (context == null){
            return "";
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        return sp.getString("setTemperature", "");
    }


    public static void setTechnology(Context context, String deviceId){
        if (context == null){
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("setTechnology", deviceId);
        editor.apply();
    }

    @NonNull
    public static String getTechnology(Context context){
        if (context == null){
            return "";
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        return sp.getString("setTechnology", "");
    }

    public static void setOutIp(Context context, String deviceId){
        if (context == null){
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("setOutIp", deviceId);
        editor.apply();
    }

    @NonNull
    public static String getOutIp(Context context){
        if (context == null){
            return "";
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        return sp.getString("setOutIp", "");
    }

    public static void setPhoneNum(Context context, String phoneNum){
        if (context == null){
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("setPhoneNum", phoneNum);
        editor.apply();
    }

    @NonNull
    public static String getPhoneNum(Context context){
        if (context == null){
            return "";
        }
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, 0);
        return sp.getString("setPhoneNum", "");
    }

}
