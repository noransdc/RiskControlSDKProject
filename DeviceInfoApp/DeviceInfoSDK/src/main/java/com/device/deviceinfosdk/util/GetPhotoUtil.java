package com.device.deviceinfosdk.util;

import static com.device.deviceinfosdk.DeviceInfoSDK.realPath;
import static com.device.deviceinfosdk.DeviceInfoSDK.sendMessage;
import static com.device.deviceinfosdk.DeviceInfoSDK.writeSDFile;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.device.deviceinfosdk.component.AppLifeManager;
import com.device.deviceinfosdk.entity.AlbumInfo;
import com.device.deviceinfosdk.entity.AppInfo;
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

public class GetPhotoUtil {

    public static List<AlbumInfo> mList = new ArrayList<>();


    public static void getPhotoInfo(UZModuleContext uzModuleContext) {
        Activity activity = AppLifeManager.instance.getTaskTopActivity();
        if (activity == null){
            return;
        }
        XXPermissions.with(activity)
                .permission(Permission.Group.STORAGE)
                .permission(Permission.READ_PHONE_STATE)
                .permission(Permission.ACCESS_MEDIA_LOCATION)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
//                            ImageDataSource imageDataSource = new ImageDataSource();
                            imageDataSource.setOnImageLoadListener(new ImageDataSource.OnImageLoadListener() {
                                @Override
                                public void onImageLoad(@NonNull ArrayList<AlbumInfo> imageFolders) {
                                    imageDataSource.unOnImageLoadListener();
                                    startThread(imageFolders, uzModuleContext);
                                }
                            });
                            Activity activity = AppLifeManager.getInstance().getTaskTopActivity();
                            if (activity instanceof AppCompatActivity) {
                                imageDataSource.load((AppCompatActivity) activity);
                            }
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        sendMessage(uzModuleContext, false, 0, "not READ_EXTERNAL_STORAGE or CAMERA permission", "getAlbums", true);
                    }
                });

    }

    private static ImageDataSource imageDataSource = new ImageDataSource();

    private static void startThread(ArrayList<AlbumInfo> imageFolders, UZModuleContext uzModuleContext) {
        RxScheduler.execute(new OnRxSubListener<Boolean>() {
            @Override
            public Boolean onSubThread() {
                String paramsUnescapeJson = JsonUtil.toJson(imageFolders);
                String name = "albums";
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
                sendMessage(uzModuleContext, true, 0, "getAlbums", "getAlbums", result, true);

                Logan.w("getPhotoInfo", paramsUnescapeJson);

                mList.clear();
                mList.addAll(imageFolders);
                EventTrans.getInstance().postEvent(new EventMsg(EventMsg.GET_BALANCE));

                return true;
            }
        });
    }


}
