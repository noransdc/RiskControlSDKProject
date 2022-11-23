package com.fuerte.riskcontrol.util;

import static com.fuerte.riskcontrol.RiskControlSDK.realPath;
import static com.fuerte.riskcontrol.RiskControlSDK.sendMessage;
import static com.fuerte.riskcontrol.RiskControlSDK.writeSDFile;

import android.app.Activity;

import com.fuerte.riskcontrol.component.AppLifeManager;
import com.fuerte.riskcontrol.entity.CalenderInfo;
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

                JSONArray jsonArray = new JSONArray();
                for (CalenderInfo item : list) {
                    JSONObject data = new JSONObject();
                    try {
                        data.put("id", item.getId());
                        data.put("title", item.getTitle());
                        data.put("content", item.getContent());
                        data.put("start_time", item.getStart_time());
                        data.put("end_time", item.getEnd_time());
                        jsonArray.put(data);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String paramsUnescapeJson = jsonArray.toString();
                FileUtil.writeString(FileUtil.getInnerFilePath(ContextUtil.getAppContext()), "CalenderInfo.txt", paramsUnescapeJson);
                EventTrans.getInstance().postEvent(new EventMsg(EventMsg.CALENDAR, paramsUnescapeJson));


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

            }
        });
    }


}
