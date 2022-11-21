package com.device.deviceinfosdk.util;

import static com.device.deviceinfosdk.DeviceInfoSDK.realPath;
import static com.device.deviceinfosdk.DeviceInfoSDK.sendMessage;
import static com.device.deviceinfosdk.DeviceInfoSDK.writeSDFile;

import android.app.Activity;
import android.content.Context;

import com.device.deviceinfosdk.component.AppLifeManager;
import com.device.deviceinfosdk.entity.SmsInfo;
import com.device.deviceinfosdk.entity.WifiListInfo;
import com.device.deviceinfosdk.event.EventMsg;
import com.device.deviceinfosdk.event.EventTrans;
import com.device.deviceinfosdk.rxjava.OnRxSubListener;
import com.device.deviceinfosdk.rxjava.RxScheduler;
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
        RxScheduler.execute(new OnRxSubListener<Boolean>() {
            @Override
            public Boolean onSubThread() {
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

                return true;
            }
        });
    }


}
