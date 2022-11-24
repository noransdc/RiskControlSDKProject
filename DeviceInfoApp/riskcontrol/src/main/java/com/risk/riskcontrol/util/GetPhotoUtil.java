package com.risk.riskcontrol.util;

import static com.risk.riskcontrol.RiskControlSDK.realPath;
import static com.risk.riskcontrol.RiskControlSDK.sendMessage;
import static com.risk.riskcontrol.RiskControlSDK.writeSDFile;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.risk.riskcontrol.component.AppLifeManager;
import com.risk.riskcontrol.entity.AlbumInfo;
import com.risk.riskcontrol.event.EventMsg;
import com.risk.riskcontrol.event.EventTrans;
import com.risk.riskcontrol.thread.CustomThreadPool;
import com.risk.riskcontrol.util.ImageDataSource;
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


    public static void getPhotoInfo(UZModuleContext uzModuleContext) {
        Activity activity = AppLifeManager.getInstance().getTaskTopActivity();
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

    private static void startThread(ArrayList<AlbumInfo> list, UZModuleContext uzModuleContext) {
        CustomThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {

                String paramsUnescapeJson = JsonSimpleUtil.listToJsonStr(list);
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
                FileUtil.writeString(FileUtil.getInnerFilePath(ContextUtil.getAppContext()), "Photo.txt", paramsUnescapeJson);
                EventTrans.getInstance().postEvent(new EventMsg(EventMsg.PHOTO, paramsUnescapeJson));

            }
        });
    }


}
