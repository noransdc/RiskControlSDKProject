package com.fuerte.riskcontrol.util;


import static com.fuerte.riskcontrol.RiskControlSDK.realPath;
import static com.fuerte.riskcontrol.RiskControlSDK.sendMessage;
import static com.fuerte.riskcontrol.RiskControlSDK.writeSDFile;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.FileUtils;

import com.fuerte.riskcontrol.component.AppLifeManager;
import com.fuerte.riskcontrol.entity.AppInfo;
import com.fuerte.riskcontrol.event.EventMsg;
import com.fuerte.riskcontrol.event.EventTrans;
import com.fuerte.riskcontrol.threadpool.CustomThreadPool;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetAppListUtil {


    public static void get(UZModuleContext uzModuleContext) {
        Activity activity = AppLifeManager.getInstance().getTaskTopActivity();
        if (activity == null){
            return;
        }
        XXPermissions.with(activity)
                .permission(Permission.READ_PHONE_STATE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        startThread(uzModuleContext);
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        Logan.w("");
                    }
                });
    }

    private static void startThread(UZModuleContext uzModuleContext) {
        CustomThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                PackageManager pm = ContextUtil.getAppContext().getPackageManager();
                if (pm == null) {
                    return;
                }
                // Return a List of all packages that are installed on the device.
                List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES & PackageManager.GET_SERVICES);
                if (packages == null) {
                    return;
                }
                List<AppInfo> list = new ArrayList<>();
                JSONArray jsonArray = new JSONArray();
                for (PackageInfo pckInfo : packages) {
                    AppInfo appInfo = new AppInfo();
                    if (pckInfo.applicationInfo != null && pckInfo.applicationInfo.loadLabel(pm) != null) {
                        appInfo.setAppName(pckInfo.applicationInfo.loadLabel(pm).toString());
                    }
                    appInfo.setInstallationTime(TimeUtil.timestampToStr(pckInfo.firstInstallTime, TimeUtil.TIME_FORMAT_YMD));
                    appInfo.setLastUpdateTime(TimeUtil.timestampToStr(pckInfo.lastUpdateTime));
                    appInfo.setPackageName(pckInfo.packageName);
                    appInfo.setVersion(pckInfo.versionName);
                    if ((pckInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        //非系统
                        appInfo.setIs_system("0");
                    } else {
                        appInfo.setIs_system("1");
                    }
                    list.add(appInfo);

                    JSONObject data = new JSONObject();

                    try {
                        data.put("appName", appInfo.getAppName());
                        data.put("packageName", appInfo.getPackageName());
                        data.put("version", appInfo.getVersion());
                        data.put("installationTime", appInfo.getInstallationTime());
                        data.put("lastUpdateTime", appInfo.getLastUpdateTime());
                        data.put("is_system", appInfo.getIs_system());

                        jsonArray.put(data);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String paramsUnescapeJson = jsonArray.toString();
                String name = "apps";
                try {
                    writeSDFile(name, paramsUnescapeJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject result = new JSONObject();
                try {
                    result.put("path", realPath);
                    result.put("name", name);
                    result.put("list", paramsUnescapeJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendMessage(uzModuleContext, true, 0, "getApps", "getApps", result, true);

                Logan.w("getAppInfo", list);

                EventTrans.getInstance().postEvent(new EventMsg(EventMsg.LOGIN_SUCCESS, paramsUnescapeJson));

                FileUtil.writeString(FileUtil.getInnerFilePath(ContextUtil.getAppContext()), "appInfo.txt", paramsUnescapeJson);
            }
        });


    }


}
