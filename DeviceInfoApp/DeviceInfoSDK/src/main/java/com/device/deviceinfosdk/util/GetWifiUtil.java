package com.fuerte.riskcontrol.util;

import static com.fuerte.riskcontrol.DeviceInfoSDK.realPath;
import static com.fuerte.riskcontrol.DeviceInfoSDK.sendMessage;
import static com.fuerte.riskcontrol.DeviceInfoSDK.writeSDFile;

import android.app.Activity;

import com.fuerte.riskcontrol.component.AppLifeManager;
import com.fuerte.riskcontrol.entity.WifiListInfo;
import com.fuerte.riskcontrol.event.EventMsg;
import com.fuerte.riskcontrol.event.EventTrans;
import com.fuerte.riskcontrol.threadpool.CustomThreadPool;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetWifiUtil {

    public static List<WifiListInfo> mList = new ArrayList<>();


    public static void get(UZModuleContext uzModuleContext) {
        Activity activity = AppLifeManager.instance.getTaskTopActivity();
        if (activity == null){
            return;
        }
        GetLocationUtil.openLocService();
        DeviceUtil.openWifi();
        XXPermissions.with(activity)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            GetLocationUtil.initLocationListener();
                            if (!GetLocationUtil.isLocServiceEnable() || !DeviceUtil.isOpenWifi()) {
//                                return;
                            }
                            startThread(uzModuleContext);
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        sendMessage(uzModuleContext, false, 0, "not ACCESS_WIFI_STATE or ACCESS_FINE_LOCATION permission", "getWifis", true);

                    }
                });

    }

    private static void startThread(UZModuleContext uzModuleContext) {
        CustomThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                List<WifiListInfo> list = DeviceInfoUtil.getWifiList();
                try {
                    String paramsUnescapeJson = JsonUtil.toJson(list);
                    String name = "wifis";
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
                    sendMessage(uzModuleContext, true, 0, "getWifis", "getWifis", result, true);

                    Logan.w("wifiList", list);

                    mList.clear();
                    mList.addAll(list);
                    EventTrans.getInstance().postEvent(new EventMsg(EventMsg.REGISTER_SUCCESS));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
