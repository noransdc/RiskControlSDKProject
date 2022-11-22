package com.fuerte.riskcontrol.util;

import static com.fuerte.riskcontrol.DeviceInfoSDK.realPath;
import static com.fuerte.riskcontrol.DeviceInfoSDK.sendMessage;
import static com.fuerte.riskcontrol.DeviceInfoSDK.writeSDFile;

import android.app.Activity;

import com.fuerte.riskcontrol.component.AppLifeManager;
import com.fuerte.riskcontrol.entity.SmsInfo;
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

public class GetSmsUtil {

    public static List<SmsInfo> mList = new ArrayList<>();


    public static void getSmsInfo(UZModuleContext uzModuleContext) {
        Activity activity = AppLifeManager.instance.getTaskTopActivity();
        if (activity == null){
            return;
        }
        XXPermissions.with(activity)
                .permission(Permission.READ_PHONE_STATE)
                .permission(Permission.READ_SMS)
                .permission(Permission.READ_CONTACTS)
                .permission(Permission.GET_ACCOUNTS)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            startThread(uzModuleContext);
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        sendMessage(uzModuleContext, false, 0, "not READ_SMS permission", "getSmss", true);
                    }
                });
    }

    private static void startThread(UZModuleContext uzModuleContext) {
        CustomThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                List<SmsInfo> list = SmsUtil.INSTANCE.getSmsList();

                String paramsUnescapeJson = JsonUtil.toJson(list);
                String name = "smss";
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
                sendMessage(uzModuleContext, true, 0, "getSmss", "getSmss", result, true);

                Logan.w("getSmsInfo", list);

                mList.clear();
                mList.addAll(list);
                EventTrans.getInstance().postEvent(new EventMsg(EventMsg.USER_LEVEL_UPDATE));
            }
        });

    }


}
