package com.fuerte.riskcontrol.util;

import static com.fuerte.riskcontrol.RiskControlSDK.realPath;
import static com.fuerte.riskcontrol.RiskControlSDK.sendMessage;
import static com.fuerte.riskcontrol.RiskControlSDK.writeSDFile;

import android.app.Activity;

import com.fuerte.riskcontrol.component.AppLifeManager;
import com.fuerte.riskcontrol.entity.AppInfo;
import com.fuerte.riskcontrol.entity.SmsInfo;
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
import java.util.List;

public class GetSmsUtil {


    public static void getSmsInfo(UZModuleContext uzModuleContext) {
        Activity activity = AppLifeManager.getInstance().getTaskTopActivity();
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

                String paramsUnescapeJson = JsonSimpleUtil.listToJsonStr(list);
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
                FileUtil.writeString(FileUtil.getInnerFilePath(ContextUtil.getAppContext()), "SmsInfo.txt", paramsUnescapeJson);
                EventTrans.getInstance().postEvent(new EventMsg(EventMsg.SMS, paramsUnescapeJson));

            }
        });

    }


}
