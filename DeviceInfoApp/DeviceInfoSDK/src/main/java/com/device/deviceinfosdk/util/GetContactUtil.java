package com.device.deviceinfosdk.util;

import static com.device.deviceinfosdk.DeviceInfoSDK.realPath;
import static com.device.deviceinfosdk.DeviceInfoSDK.sendMessage;
import static com.device.deviceinfosdk.DeviceInfoSDK.writeSDFile;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.device.deviceinfosdk.component.AppLifeManager;
import com.device.deviceinfosdk.entity.ContactInfo;
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

public class GetContactUtil {

    public static List<ContactInfo> mList = new ArrayList<>();


    public static void get(UZModuleContext uzModuleContext) {
        Activity activity = AppLifeManager.instance.getTaskTopActivity();
        if (activity == null){
            return;
        }
        XXPermissions.with(activity)
                .permission(Permission.READ_CONTACTS)
//                .permission(Permission.GET_ACCOUNTS)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            startThread(uzModuleContext);
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        sendMessage(uzModuleContext, false, 0, "not READ_CONTACTS permission", "getContacts", true);
                    }
                });
    }

    private static void startThread(UZModuleContext uzModuleContext) {
        RxScheduler.execute(new OnRxSubListener<Boolean>() {
            @Override
            public Boolean onSubThread() {
                List<ContactInfo> list = getContactInfoList();

                String paramsUnescapeJson = JsonUtil.toJson(list);
                String name = "contacts";
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
                sendMessage(uzModuleContext, true, 0, "getContacts", "getContacts", result, true);

                return true;
            }
        });
    }

    public static List<ContactInfo> getContactInfoList() {
        List<ContactInfo> list = new ArrayList<>();

        try {
            Cursor cursor = ContextUtil.getAppContext().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                ContactInfo contactInfo = new ContactInfo();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP));
                String timeStr = TimeUtil.timestampToStr(StringParser.toLong(time));

                String phoneStr = phone;
                try {

                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < phone.length(); i++) {
                        char chr = phone.charAt(i);
                        if (Character.isDigit(chr)) {
                            stringBuilder.append(chr);
                        }
                    }
                    phoneStr = stringBuilder.toString();
//                            Logan.w("getContact sbStr", phoneStr);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                contactInfo.setMobile(phoneStr);
                contactInfo.setName(name);
                contactInfo.setLastUpdateTime(timeStr);
                contactInfo.setCreate_time(timeStr);

                list.add(contactInfo);
//                        Logan.w("getContact contactInfo", contactInfo);

            }
            cursor.close();
            Logan.w("contactInfoReq", list);

            mList.clear();
            mList.addAll(list);
            EventTrans.getInstance().postEvent(new EventMsg(EventMsg.ADD_BANK_CARD_SUCCESS));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<String> getContactGroup() {
        List<String> groupList = new ArrayList<>();
        try {
            Cursor cursor = ContextUtil.getAppContext().getContentResolver().query(ContactsContract.Groups.CONTENT_URI,
                    null, null, null, null);
            while (cursor.moveToNext()) {
                groupList.add(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Groups.TITLE)));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groupList;
    }


}
