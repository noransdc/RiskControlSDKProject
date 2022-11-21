package com.device.deviceinfosdk;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.device.deviceinfosdk.component.SpConstant;
import com.device.deviceinfosdk.util.ContextUtil;
import com.device.deviceinfosdk.util.GetAppListUtil;
import com.device.deviceinfosdk.util.GetCalenderUtil;
import com.device.deviceinfosdk.util.GetContactUtil;
import com.device.deviceinfosdk.util.GetDeviceInfoUtil;
import com.device.deviceinfosdk.util.GetLocationUtil;
import com.device.deviceinfosdk.util.GetPhotoUtil;
import com.device.deviceinfosdk.util.GetSmsUtil;
import com.device.deviceinfosdk.util.GetWifiUtil;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DiyMInfo extends UZModule {
    private static String TAG = "DiyMInfo";

    public static String orderno = "";
    public static String realPath = "";


    private UZModuleContext calendarsContext;
    private UZModuleContext albumContext;
    private UZModuleContext smsContext;
    private UZModuleContext appContext;
    private JSONArray packages;
    private UZModuleContext contactContext;
    private UZModuleContext geoInfoContext;
    private UZModuleContext wifiContext;
    private UZModuleContext deviceInfoContext;

    public DiyMInfo(UZWebView webView) {
        super(webView);
//        eventReceived = new EventReceived();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(EVENTRECEIVED_ACTION);
//        mContext.registerReceiver(eventReceived, intentFilter);
//        Log.d(TAG, "DiyMInfo-version: 20220801");
        Log.d(TAG, "DiyMInfo-version: 20220921");
    }

    public void sendMessage(UZModuleContext context, boolean status, int code, String msg, String type, boolean deleteJsFunction) {
        if (context == null){
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

    public void sendMessage(UZModuleContext context, boolean status, int code, String msg, String type, JSONObject result, boolean deleteJsFunction) {
        if (context == null){
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

    public void writeSDFile(String keyName, String content) throws IOException {

//        File file = new File(realPath);
//        FileOutputStream fos = new FileOutputStream(file);
//        byte [] bytes = write_str.getBytes();
//        fos.write(bytes);
//        fos.close();
        String[] PERMISSION_LIST = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        List<String> needRequestList = checkPermission(ContextUtil.getAppContext(), PERMISSION_LIST);
        if (needRequestList.isEmpty()) {

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
//            StringEscapeUtils.unescapeJson(lastContent);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(lastContent.getBytes());
            outputStream.close();
        } else {
            Log.d(TAG, "not READ_EXTERNAL_STORAGE or WRITE_EXTERNAL_STORAGE permission");
        }
    }

    public void jsmethod_init(final UZModuleContext moduleContext) {
        String path = moduleContext.optString("path");
        orderno = moduleContext.optString("orderno");
        if (!TextUtils.isEmpty(path)) {
            realPath = makeRealPath(path);
        } else {
            realPath = Environment.getExternalStorageDirectory().getPath() + "/diyminfo.json";

        }
        //清空文件里面数据


        if (TextUtils.isEmpty(orderno)) {
            orderno = "IF" + System.currentTimeMillis();
        }


        JSONObject result = new JSONObject();
        try {
            result.put("path", realPath);
            result.put("orderno", orderno);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendMessage(moduleContext, true, 0, "onInit", "onInit", result, true);
    }

    //    1.2.6 device_info 设备信息
//    https://www.jianshu.com/p/ca869aa2fd72
    public void jsmethod_getDeviceInfo(final UZModuleContext moduleContext) {
        deviceInfoContext = moduleContext;
        GetDeviceInfoUtil.get(deviceInfoContext);
    }


    public void jsmethod_getBatteryInfo(final UZModuleContext moduleContext) {

        JSONObject info = new JSONObject();
        try {
            info.put("technology", SpConstant.getTechnology(ContextUtil.getAppContext()));
            info.put("status", SpConstant.getStatus(ContextUtil.getAppContext()));
            info.put("health", SpConstant.getHealth(ContextUtil.getAppContext()));
            info.put("temperature", SpConstant.getTemperature(ContextUtil.getAppContext()));
            info.put("battery_max", SpConstant.getBatteryMax(ContextUtil.getAppContext()));
            info.put("battery_now", SpConstant.getBatteryNow(ContextUtil.getAppContext()));
            info.put("battery_level", SpConstant.getBatteryLevel(ContextUtil.getAppContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String infoUnescapeJson = info.toString();
        String name = "batteryInfo";
        try {
            writeSDFile(name, infoUnescapeJson);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject result = new JSONObject();
        try {
            result.put("path", realPath);
            result.put("name", name);
            result.put("info", infoUnescapeJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendMessage(moduleContext, true, 0, "getBatteryInfo", "getBatteryInfo", result, true);
    }

    //    1.2.7 geo_info   地理位置信息
    public void jsmethod_getGeoInfo(final UZModuleContext moduleContext) {

        geoInfoContext = moduleContext;

        GetLocationUtil.getLocationInfo(geoInfoContext);
    }

    //    https://www.jb51.net/article/225673.htm
    public void jsmethod_getWifis(final UZModuleContext moduleContext) {
        wifiContext = moduleContext;

        GetWifiUtil.get(wifiContext);
    }


    //    1.2.5 phonebook_info  通讯录
//    https://www.jianshu.com/p/b3e4af99c2fb
    public void jsmethod_getContacts(final UZModuleContext moduleContext) {
        contactContext = moduleContext;
        GetContactUtil.get(contactContext);
    }


//    1.2.8 applist_info  app安装信息
//    https://www.jianshu.com/p/657a53b75fd8
    public void jsmethod_getApps(final UZModuleContext moduleContext) {
        appContext = moduleContext;
        GetAppListUtil.get(appContext);
    }


    //    1.2.8 applist_info  app安装信息
//    https://www.jianshu.com/p/657a53b75fd8
    public void jsmethod_getApps2(final UZModuleContext moduleContext) {

        appContext = moduleContext;
        packages = moduleContext.optJSONArray("packages");
        new Thread(appThread2).start();

    }

    Thread appThread2 = new Thread(new Runnable() {
        @Override
        public void run() {

            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            PackageManager pm = context().getPackageManager();

            JSONArray params = new JSONArray();

            for (int i = 0; i < packages.length(); i++) {
                String packageName = null;
                try {
                    packageName = packages.getString(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(packageName.length() > 0) {

                    PackageInfo packageInfo = null;
                    try {
                        packageInfo = pm.getPackageInfo(packageName, 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "packageInfo: "+packageInfo);
                    if(packageInfo != null){
                        String is_system = "1";
                        if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0)is_system = "0";

                        String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
                        String versionName = packageInfo.versionName;
                        String firstInstallTime = sd.format(new Date(packageInfo.firstInstallTime));
                        String lastUpdateTime = sd.format(new Date(packageInfo.lastUpdateTime));
                        JSONObject tmpObj = new JSONObject();
                        try {
                            tmpObj.put("appName", appName);
                            tmpObj.put("packageName", packageName);
                            tmpObj.put("versionName", versionName);
                            tmpObj.put("is_system", is_system);
                            tmpObj.put("firstInstallTime", firstInstallTime);
                            tmpObj.put("lastUpdateTime", lastUpdateTime);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        params.put(tmpObj);
                        tmpObj = null;
                    }
                }
            }

            String paramsUnescapeJson = params.toString();
//            String paramsUnescapeJson = StringEscapeUtils.unescapeJson(params.toString());
            String name = "apps";
//            String path = Environment.getExternalStorageDirectory().getPath()+ "/" + TAG +  "/apps.json";
            try {
                writeSDFile(name,paramsUnescapeJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject result = new JSONObject();
            try {
                result.put("path",realPath);
                result.put("name",name);
                result.put("list",paramsUnescapeJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendMessage(appContext,true,0,"getApps","getApps",result,true);
        }
    });

//    1.2.9 sms_info  短信数据
//    https://www.jianshu.com/p/b11b2eb769f4
    public void jsmethod_getSmss(final UZModuleContext moduleContext) {
        smsContext = moduleContext;
        GetSmsUtil.getSmsInfo(smsContext);
    }


//    1.2.11 album_info 相册信息
//    https://www.jianshu.com/p/57487bb1ec5a
    public void jsmethod_getAlbums(final UZModuleContext moduleContext) {
        albumContext = moduleContext;
        GetPhotoUtil.getPhotoInfo(albumContext);
    }


//    calendars_info 日历信息
//    https://blog.csdn.net/u014361280/article/details/108152733
    public void jsmethod_getCalendars(final UZModuleContext moduleContext) {
        calendarsContext = moduleContext;
        GetCalenderUtil.get(calendarsContext);
    }


    private static List<String> checkPermission(Context context, String[] checkList) {
        List<String> list = new ArrayList<>();
        for (String s : checkList) {
            if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(context, s)) {
                list.add(s);
            }
        }
        return list;
    }


}
