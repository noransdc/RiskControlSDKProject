package com.fuerte.riskcontrol.util;

import static com.fuerte.riskcontrol.RiskControlSDK.realPath;
import static com.fuerte.riskcontrol.RiskControlSDK.sendMessage;
import static com.fuerte.riskcontrol.RiskControlSDK.writeSDFile;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fuerte.riskcontrol.component.AppLifeManager;
import com.fuerte.riskcontrol.entity.AlbumInfo;
import com.fuerte.riskcontrol.entity.AppInfo;
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

                JSONArray jsonArray = new JSONArray();
                for (AlbumInfo item : list) {
                    JSONObject data = new JSONObject();
                    try {
                        data.put("name", item.getName());
                        data.put("author", item.getAuthor());
                        data.put("height", item.getHeight());
                        data.put("width", item.getWidth());
                        data.put("longitude", item.getLongitude());
                        data.put("latitude", item.getLatitude());
                        data.put("model", item.getModel());
                        data.put("addTime", item.getAddTime());
                        data.put("updateTime", item.getUpdateTime());
                        data.put("save_time", item.getSave_time());
                        data.put("orientation", item.getOrientation());
                        data.put("x_resolution", item.getX_resolution());
                        data.put("y_resolution", item.getY_resolution());
                        data.put("gps_altitude", item.getGps_altitude());
                        data.put("gps_processing_method", item.getGps_processing_method());
                        data.put("lens_make", item.getLens_make());
                        data.put("lens_model", item.getLens_model());
                        data.put("focal_length", item.getFocal_length());
                        data.put("flash", item.getFlash());
                        data.put("software", item.getSoftware());
                        data.put("id", item.getId());
                        jsonArray.put(data);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String paramsUnescapeJson = jsonArray.toString();
                FileUtil.writeString(FileUtil.getInnerFilePath(ContextUtil.getAppContext()), "Photo.txt", paramsUnescapeJson);
                EventTrans.getInstance().postEvent(new EventMsg(EventMsg.PHOTO, paramsUnescapeJson));


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

            }
        });
    }


}
