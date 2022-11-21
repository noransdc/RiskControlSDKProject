package com.device.deviceinfosdk.util;

import static com.device.deviceinfosdk.DeviceInfoSDK.realPath;
import static com.device.deviceinfosdk.DeviceInfoSDK.sendMessage;
import static com.device.deviceinfosdk.DeviceInfoSDK.writeSDFile;

import android.app.Activity;
import android.content.Context;

import com.device.deviceinfosdk.component.AppLifeManager;
import com.device.deviceinfosdk.entity.AppInfo;
import com.device.deviceinfosdk.entity.CalenderInfo;
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

public class GetCalenderUtil {

    public static List<CalenderInfo> mList = new ArrayList<>();


    public static void get(UZModuleContext uzModuleContext) {
        Activity activity = AppLifeManager.instance.getTaskTopActivity();
        if (activity == null){
            return;
        }
        XXPermissions.with(activity)
                .permission(Permission.Group.CALENDAR)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            startThread(uzModuleContext);
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        sendMessage(uzModuleContext, false, 0, "not READ_CALENDAR permission", "getCalendars", true);
                    }
                });
    }

    private static void startThread(UZModuleContext uzModuleContext) {
        RxScheduler.execute(new OnRxSubListener<Boolean>() {
            @Override
            public Boolean onSubThread() {

                List<CalenderInfo> list = CalendersUtil.INSTANCE.getCalendersList();

                String paramsUnescapeJson = JsonUtil.toJson(list);
                String name = "calendars";
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
                sendMessage(uzModuleContext, true, 0, "getCalendars", "getCalendars", result, true);

                Logan.w("List<CalenderInfo>", list);

                mList.clear();
                mList.addAll(list);
                EventTrans.getInstance().postEvent(new EventMsg(EventMsg.MODIFY_NICKNAME));

                return true;
            }
        });
    }


}
