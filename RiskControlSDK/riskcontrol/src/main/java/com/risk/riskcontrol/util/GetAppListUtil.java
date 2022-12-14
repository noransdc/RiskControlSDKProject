package com.risk.riskcontrol.util;


import static com.risk.riskcontrol.RiskControl.realPath;
import static com.risk.riskcontrol.RiskControl.sendMessage;
import static com.risk.riskcontrol.RiskControl.writeSDFile;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.risk.riskcontrol.component.AppLifeManager;
import com.risk.riskcontrol.entity.AppInfo;
import com.risk.riskcontrol.event.EventMsg;
import com.risk.riskcontrol.event.EventTrans;
import com.risk.riskcontrol.thread.CustomThreadPool;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

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
                        //?????????
                        appInfo.setIs_system("0");
                    } else {
                        appInfo.setIs_system("1");
                    }
                    list.add(appInfo);

                }

                String paramsUnescapeJson = JsonSimpleUtil.listToJsonStr(list);
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
                FileUtil.writeString(FileUtil.getInnerFilePath(ContextUtil.getAppContext()), "AppInfo.txt", paramsUnescapeJson);
                EventTrans.getInstance().postEvent(new EventMsg(EventMsg.APP_INFO, paramsUnescapeJson));

            }
        });


    }


}
