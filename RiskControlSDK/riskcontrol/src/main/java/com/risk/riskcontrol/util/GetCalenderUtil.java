package com.risk.riskcontrol.util;

import static com.risk.riskcontrol.RiskControl.realPath;
import static com.risk.riskcontrol.RiskControl.sendMessage;
import static com.risk.riskcontrol.RiskControl.writeSDFile;

import android.app.Activity;

import com.risk.riskcontrol.component.AppLifeManager;
import com.risk.riskcontrol.entity.CalenderInfo;
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
import java.util.List;

public class GetCalenderUtil {


    public static void get(UZModuleContext uzModuleContext) {
        Activity activity = AppLifeManager.getInstance().getTaskTopActivity();
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
        CustomThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                List<CalenderInfo> list = CalendersUtil.INSTANCE.getCalendersList();

                String paramsUnescapeJson = JsonSimpleUtil.listToJsonStr(list);
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
                FileUtil.writeString(FileUtil.getInnerFilePath(ContextUtil.getAppContext()), "CalenderInfo.txt", paramsUnescapeJson);
                EventTrans.getInstance().postEvent(new EventMsg(EventMsg.CALENDAR, paramsUnescapeJson));

            }
        });
    }


}
